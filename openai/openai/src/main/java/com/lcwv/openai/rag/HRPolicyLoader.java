package com.lcwv.openai.rag;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HRPolicyLoader {
    private final VectorStore vectorStore;

    public HRPolicyLoader(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }
    @Value("classpath:CODEWITHVIKAS_HR_POLICY_MANUAL.pdf")
    Resource policyFile;
    @PostConstruct
    public void loadPDF(){
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(policyFile);
        List<Document> documents = tikaDocumentReader.get();
        vectorStore.add(documents);//witgh this there is a problem asd ve are not splitting the document in small chunks

    }
}
