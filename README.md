# 🎬 MovieAI

**MovieAI** is a desktop application that gives a microservices architecture to offer intelligent movie recommendations. It integrates different technologies across multiple layers:

-  **Quarkus backend (Java)** — Core services and PostgreSQL database access via JPA
-  **Flask backend (Python)** — Handles recommendation logic using IA algorithm
-  **JavaFX frontend (Java)** — Desktop graphical user interface



## 📥 How to Download This Repository

To get started, clone the repository using Git:

```bash
git clone https://github.com/ghoulitopipo/MovieAI.git
cd MovieAI
```

## 📁 Project Structure

<pre> 
MOVIEAI-(root)/
│
├── backend-(quarkus)/ 
├── Recommendation_Python-(flask)/
└── frontend-(javafx)/ 
</pre>

## 🛠️ Requirements

Make sure the following tools are installed and configured:

###  General Tools

- **Docker** (must be running before launching)
- **Python 3.8+**
  - `pip` and `virtualenv` are recommended for managing dependencies
- **Java 21+**
- **Maven** (for building both frontend and backend)

###  JavaFX Specific

- **JavaFX SDK 17.0.2** (required if not bundled with your JDK)
  - Required modules: `javafx.controls`, `javafx.fxml`
  - Set the `--module-path` in your VM options if needed

###  Port Availability

Ensure the following ports are free (not used by any other services):

- `http://localhost:8080` → used by **Quarkus backend**
- `http://localhost:5000` → used by **Flask Python API**

> ⚠️ If ports are occupied, you may need to stop conflicting services or change the configurations manually.

## 🚀 Launch Instructions

### Option 1: Manual launch

### 1️⃣ Start the Quarkus Backend

```bash
cd backend
mvnw quarkus:dev
```

### 2️⃣ Start the Flask API

```bash
cd Recommendation_Python/AI
python ApiJava.py
```

### 3️⃣ Launch the JavaFX Frontend

```bash
cd frontend
mvn javafx:run
```

---

### Option 2: Launch everything with the provided script (launch.sh)


```bash
chmod +x launch.sh
./launch.sh
```

You will see an interactive menu with options:

1. Launch the JavaFX frontend.

2. Reload the Flask API (without restarting everything)

3. Quit and cleanly shut down all services

> **Notes:**
> - Ports are checked before services start
> - Each backend waits until it's fully ready
> - Reload the Flask API (option 2) after modifying the recommendation code
> - Shut down everything cleanly with option 3
> - You can run the frontend multiple times without restarting backends

> ⚠️ **Warning:**  
> The launch process will take some time, because of the Quarkus backend (importing of all the database).  
> Please be patient while the services fully initialize before interacting with the menu.

## 📝 License

This is a private academic project created for demonstration and educational purposes only.

No part of this repository is licensed for public or commercial use.

© 2025 Pierre Nicolas, Lilian Laure, Mattéo Bonavita, Stéphane Rossi. All rights reserved.
