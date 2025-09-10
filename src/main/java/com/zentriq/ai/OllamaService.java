package com.zentriq.ai;

import java.util.List;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OllamaService {
	
	@Autowired
	OllamaChatModel ollamaModel;
	
	@Autowired
	OllamaEmbeddingModel embeddingModel;
	
	@Autowired
	VectorStore vectorStore;
	
	// create an object of OllamaChatModel
	public OllamaService(OllamaChatModel ollamaModel, 
			             OllamaEmbeddingModel embeddingModel,
			             VectorStore vectorStore) {
		this.ollamaModel = ollamaModel;
		this.embeddingModel = embeddingModel;
		this.vectorStore = vectorStore;
	}
	
	public VectorStore getVectorStore() {
        return vectorStore;
    }
	
	// Index documents into the vector store
	public void indexDocuments(List<Document> docs) {
        vectorStore.add(docs);
    }
	
	/**
     * RAG query:
     * 1. Search vector store
     * 2. Add retrieved context to the prompt
     * 3. Ask LLaMA (chat model)
     */
    public String ask(String userQuery) {
    	// 1. Try to retrieve context from vector store
        SearchRequest searchRequest = SearchRequest.builder()
                .query(userQuery)
                .topK(3)
                .similarityThreshold(0.2)
                .build();

        List<Document> similarDocs = vectorStore.similaritySearch(searchRequest);

        StringBuilder context = new StringBuilder();
        for (Document doc : similarDocs) {
            context.append(doc.getText()).append("\n---\n");
        }

        String fullPrompt;

        // 2. Decide which path to take
        if (!similarDocs.isEmpty()) {
            // ✅ Found context → use RAG
            fullPrompt = """
                You are a helpful AI assistant.
                Use the following context to answer the question.
                Context:
                %s
                Question: %s
                """.formatted(context, userQuery);
        } else {
            // ❌ No useful docs → fallback to LLM knowledge
            fullPrompt = """
                You are a helpful AI assistant.
                Answer the following question using your own knowledge.
                Question: %s
                """.formatted(userQuery);
        }

        // 3. Call Ollama
        ChatResponse response = ollamaModel.call(new Prompt(fullPrompt));
        return response.getResult().getOutput().getText();
    }
}
