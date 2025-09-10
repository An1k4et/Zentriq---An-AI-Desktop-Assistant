<p align="center">
  <img src="assets/logo.png" alt="Zentriq Logo" width="120" height="120">
</p>

<h1 align="center">Zentriq – AI Desktop Assistant</h1>

<p align="center">
  An AI-powered <b>desktop assistant</b> built with <b>JavaFX</b> and <b>Spring Boot</b>,  
  integrating <b>Ollama LLMs</b> and <b>RAG</b> for context-aware document understanding.
</p>

---

## 🚀 Introduction  

- Zentriq is an **AI-powered desktop assistant** designed to combine a modern **JavaFX interface** with the power of **Spring Boot backend services**.  
It integrates **Large Language Models (LLMs)** via **Spring AI (Ollama)** and enhances responses with **Retrieval-Augmented Generation (RAG)** by blending document knowledge with AI reasoning.
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

---

## 🛠️ Tech Stack

- **Frontend**: JavaFX  
- **Backend**: Spring Boot 3, Spring AI  
- **AI/LLM**: Ollama (LLaMA models, Embedding models)  
- **RAG**: Spring AI Vector Store  
- **Data Processing**: Apache PDFBox, OpenCSV  
- **Build Tool**: Maven  

---

## 🚀 How It Works (Flow)

1. User asks a question via the **JavaFX UI**.  
2. Query is passed to the **OllamaService (Spring Boot)**.  
3. Service performs **Vector Store similarity search** for context.  
4. Constructs a **final enriched prompt** (Context + Question).  
5. Sends prompt to the **Ollama LLM**.  
6. Response is returned and displayed in the **desktop chat interface**.  

---

## 🔮 Future Enhancements
- Multi-document support with advanced indexing.  
- Role-based access and authentication.  
- Cloud vector store integration (Pinecone, Milvus).  
- Voice input and speech synthesis.  

---

## 📌 Conclusion
Zentriq demonstrates how **desktop applications** can seamlessly integrate with **modern AI frameworks**.  
It combines **Spring Boot reliability**, **JavaFX UI**, and **Spring AI + RAG architecture** to build a scalable, intelligent, and user-friendly AI assistant.
