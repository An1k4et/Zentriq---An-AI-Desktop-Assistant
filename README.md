# Zentriq – AI Desktop Assistant

Zentriq is an **AI-powered desktop assistant** built using **JavaFX** and **Spring Boot**.  
It integrates **Large Language Models (LLMs)** via **Spring AI (Ollama)** and enhances responses with **Retrieval-Augmented Generation (RAG)** by combining local document context with AI-powered reasoning.

---

## ✨ Key Features
- 🖥️ **Desktop Application** – User-friendly interface built with **JavaFX**.  
- 🤖 **LLM Integration** – Uses **Ollama models** (e.g., `llama3`, `nomic-embed-text`) through **Spring AI**.  
- 📄 **Document Understanding** – Supports **PDF parsing** (Apache PDFBox) and **text ingestion**.  
- 🔍 **RAG (Retrieval-Augmented Generation)** – Embeds documents into a **Vector Store** and retrieves relevant context for queries.  
- 🔐 **Secure & Configurable** – Credentials and model configs handled via Spring profiles.  
- ⚡ **Real-time Chat Interface** – Smooth interaction with AI inside the desktop app.  

---

## 🏗️ Architecture Overview

Zentriq combines **desktop UI + backend AI services** in a modular way:

1. **JavaFX Frontend (UI Layer)**  
   - Provides the **chat interface**.  
   - Handles **user input** and displays AI responses.  
   - Implements simple event-driven interactions (send button, enter key).  

2. **Spring Boot Backend (Service Layer)**  
   - Manages AI workflows.  
   - Coordinates between **OllamaChatModel**, **EmbeddingModel**, and **Vector Store**.  
   - Ensures scalable and testable code structure.  

3. **Ollama LLM Integration (AI Layer)**  
   - Connects to a **locally running Ollama server** (`http://localhost:11434`).  
   - Supports **chat models** (`llama3`) for reasoning and **embedding models** (`nomic-embed-text`) for vectorization.  

4. **RAG Pipeline (Knowledge Layer)**  
   - Documents (PDF/TXT) are parsed and indexed into a **Vector Store**.  
   - When a query arrives:  
     - Similar docs are retrieved (`similaritySearch`).  
     - Retrieved context is merged with the query.  
     - Final enriched prompt is passed to the LLM for an accurate, context-aware response.  
