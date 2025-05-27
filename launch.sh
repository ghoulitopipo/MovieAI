#!/bin/bash

check_port_free() {
  if lsof -i :$1 >/dev/null; then
    echo "❌ Port $1 is already in use. Please free it before launching."
    exit 1
  fi
}

wait_for_port() {
  local port=$1
  echo "⏳ Waiting for port $port to open..."
  while ! nc -z localhost "$port"; do
    sleep 1
  done
  echo "✅ Port $port is now open."
}

# Ensure required ports are free before launching
check_port_free 8080
check_port_free 5000

PIDS=()
FLASK_PID=
QUARKUS_PID=

cleanup() {
  echo -e "\n🛑 Shutting down all services..."

  for pid in "${PIDS[@]}"; do
    if kill "$pid" 2>/dev/null; then
      echo "🗑️ Killed process $pid"
      sleep 2
      if kill -0 "$pid" 2>/dev/null; then
        echo "🗑️ Process $pid still alive, force killing..."
        kill -9 "$pid"
      fi
    fi
  done

  if [[ -n "$FLASK_PID" ]]; then
    if kill "$FLASK_PID" 2>/dev/null; then
      echo "🗑️ Killed Flask process $FLASK_PID"
      sleep 2
      if kill -0 "$FLASK_PID" 2>/dev/null; then
        echo "🗑️ Flask process $FLASK_PID still alive, force killing..."
        kill -9 "$FLASK_PID"
      fi
    fi
  fi

  if [[ -n "$QUARKUS_PID" ]]; then
    PGID=$(ps -o pgid= "$QUARKUS_PID" | grep -o '[0-9]*' | head -n 1)
    echo "🛑 Terminating Quarkus process group $PGID..."
    kill -TERM -- -"$PGID" 2>/dev/null
    sleep 2
    if kill -0 "$QUARKUS_PID" 2>/dev/null; then
      echo "🛑 Quarkus still alive, force killing process group..."
      kill -9 -- -"$PGID"
    fi
  fi

  echo "🧹 Checking and killing anything still using port 8080..."
  for pid in $(lsof -ti:8080); do
    kill -9 "$pid" && echo "🗑️ Killed leftover process on port 8080 (PID $pid)"
  done

  echo "🧹 Checking and killing anything still using port 5000..."
  for pid in $(lsof -ti:5000); do
    kill -9 "$pid" && echo "🗑️ Killed leftover process on port 5000 (PID $pid)"
  done

  # Delete quarkus.log if it exists
  if [[ -f quarkus.log ]]; then
    echo "🧹 Removing quarkus.log file..."
    rm -f quarkus.log
  fi

  exit 0
}

trap cleanup SIGINT

start_quarkus() {
  echo "▶️ Starting Quarkus backend..."
  cd backend || { echo "❌ backend directory not found"; exit 1; }
  
  # Crée un fichier vide pour éviter erreur tail
  touch quarkus.log
  
  ./mvnw quarkus:dev > quarkus.log 2>&1 < /dev/null &
  QUARKUS_PID=$!
  PIDS+=($QUARKUS_PID)

  # On attend que le port 8080 soit ouvert (après démarrage)
  wait_for_port 8080

  # On lit le fichier de logs en temps réel, on arrête quand on voit la ligne clé
  tail -n +1 -f quarkus.log | while read -r line; do
    echo "$line"
    if [[ "$line" == *"Importation des ratings terminée."* ]]; then
      echo "✅ Quarkus import finished."
      pkill -P $$ tail  # tue le tail lancé ici
      break
    fi
  done

  # Supprime le log maintenant qu'on a fini
  rm -f quarkus.log

  cd - > /dev/null || true
}

start_flask() {
  echo "▶️ Starting Flask API..."
  (cd Recommendation_Python/AI && python3 ApiJava.py) &
  FLASK_PID=$!
  wait_for_port 5000
}

reload_flask() {
  echo "🔄 Reloading Flask API..."
  if [[ -n "$FLASK_PID" ]]; then
    kill "$FLASK_PID" 2>/dev/null && echo "🗑️ Killed old Flask process $FLASK_PID"
    wait "$FLASK_PID" 2>/dev/null
  fi
  (cd Recommendation_Python/AI && python3 ApiJava.py) &
  FLASK_PID=$!
  wait_for_port 5000
  echo "✅ Flask API reloaded and ready."
}

run_frontend() {
  echo "▶️ Launching JavaFX frontend..."
  (cd frontend && mvn javafx:run)
}

# Lancement services
start_quarkus
start_flask

# Affiche le menu seulement APRES chargements terminés
while true; do
  echo ""
  echo "💡 Options:"
  echo "1. Run JavaFX frontend"
  echo "2. Reload Flask API"
  echo "3. Quit and shut down all services"
  read -p "Choose an option [1-3]: " choice

  case $choice in
    1)
      run_frontend
      ;;
    2)
      reload_flask
      ;;
    3)
      cleanup
      ;;
    *)
      echo "❌ Invalid choice. Enter 1, 2, or 3."
      ;;
  esac
done
