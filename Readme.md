# 🚀 Spring AI Integrations with Spring Boot

This repository contains multiple **Spring Boot projects leveraging Spring AI** to integrate with different AI providers.  
Each project demonstrates how to configure, run, and test chat APIs using **Spring Boot + Spring AI**.  
A **Postman Collection** is also included for easy API testing.  

---

## 📂 Projects Included

### 1️⃣ OpenAI (Default Chat Model in Spring AI)
- Integrated GPT models with Spring Boot.  
- OpenAI is the **default chat model supported by Spring AI**, making setup simple and quick.  
- Perfect for cloud-based AI chat APIs.

---

### 2️⃣ Ollama
- Integrated **Llama3.2:1B** locally with Spring Boot.  
- Runs completely **on your machine**, no external API required.  
- Great for local experiments and privacy-friendly use cases.

---

### 3️⃣ Docker Model (Gemma 3)
- Deployed **Gemma 3 locally inside Docker**.  
- Integrated with Spring Boot via Spring AI.  
- Highlights **portability** and **containerized AI workflows**.

---

### 4️⃣ AWS Bedrock (Gemini - deepseek.v3-v1:0)
- Integrated **AWS Bedrock** models with Spring Boot.  
- Uses secure AWS configuration (`region`, `access-key`, `secret-key`).  
- Ideal for **enterprise-scale** AI deployments.  

---

### 5️⃣ Multi-Model (OpenAI + Ollama Combined)
- Configured **multiple ChatClients** (OpenAI + Ollama) inside one Spring Boot app.  
- Demonstrates **flexibility** and easy **provider switching/comparison**.  
- Useful for benchmarking and fallback strategies.

---

## ⚙️ Tech Stack
- **Java 17+**
- **Spring Boot 3.x**
- **Spring AI**
- **Maven**
- **Docker** (for Gemma 3)
- **AWS Bedrock** (for Gemini)
- **Postman** (for API testing)


