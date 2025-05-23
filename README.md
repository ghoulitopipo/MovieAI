# 🎬 MovieAI

This project consists of a multi-tier architecture with:

- ⚙️ Quarkus backend (Java) for core services and data (connexion with postgres thanks to jpa)
- 🐍 Flask backend (Python) for recommendation algorithm
- 💻 JavaFX frontend (Java) for the desktop user interface

## 📁 Project Structure

<pre> 
MOVIEAI-(root)/
│
├── backend-(quarkus)/ 
├── Recommendation_Python-(flask)/
└── frontend-(javafx)/ 
</pre>



## 🚀 Launch Instructions

> Prerequisites: docker launched and make sure that those port are free:

- "http://localhost:8080"

- "http://localhost:5000"

### Option 1: Manual launch

### 1️⃣ Start the Quarkus Backend

> Prerequisites: Java 17+, Maven

```bash
cd backend
mvnw quarkus:dev
```

### 2️⃣ Start the Flask API

> Prerequisites: Python 3.8+, pip, virtualenv (recommended)

```bash
cd Recommendation_Python/AI
python ApiJava.py
```

### 3️⃣ Launch the JavaFX Frontend

> Prerequisites: JavaFX SDK (and set PATH_TO_FX if necessary), Java 17+

### If using an IDE (e.g., IntelliJ, Eclipse)

1. Open the `frontend` project.
2. Set **VM options** if needed:

   ```bash
   --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml
   ```

```bash
cd frontend
mvn javafx:run
```
### Option 2: Launch everything with the provided script (launch.sh)


```bash
chmod +x launch.sh
./launch.sh
```

You will see an interactive menu with options:

1. Launch the JavaFX frontend.

2. Reload the Flask API without restarting the whole stack.

3. Quit and shut down all running services cleanly.

> **Notes:**
> - The script checks that required ports (8080 for Quarkus, 5000 for Flask) are free before starting.
> - It waits for each backend to be fully ready before continuing.
> - Use option 2 to reload the Python Flask API if you change the recommendation code, without restarting Quarkus or JavaFX.
> - Option 3 cleanly shuts down all running processes, no manual Ctrl+C needed.
> - You can run the frontend multiple times without restarting backends.

> ⚠️ **Warning:**  
> The launch process may take some time, especially when starting the Quarkus backend for the first time or after code changes.  
> Please be patient while the services fully initialize before interacting with the application.

