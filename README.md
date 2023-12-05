# HiK-Server
Rest API server implemented with Spring boot.

## Technology
- 
## Pattern
- **MVC Architecture Pattern**
  
There is a Controller layer that receives requests from clients (View) and a service layer (Model) that receives requests from the controller and performs business logic. Translator, Learning Contents, Login, and User related functions are all performed through the MVC architecture pattern, and a repository layer is implemented to implement methods to access the DB.

- **Chain of Responsibility**
  
In the process of selecting a prompt to run the gpt API, it was necessary to branch the prompt for optimization by location and target. Using the Chain of Responsibility pattern, different prompts depending on the location were created using the abstract class PromptHandler class and each prompt inheriting the PromptHandler class. Handler classes and the handleRequest method were implemented and provided.

- **Object Oriented Programming**

There are User, Sentence, and LearningContent entities. Entities are each encapsulated, and getters must be used to access them.

## Getting Started
### Requirements
- Google Cloud Platform - Register Login Api and TTS Api
- OpenAI - Chat GPT Api Key
- Amazon RDS S3
### Prerequisites
- Spring boot 2.7.x
### Step-by-Step Building Instruction
** You have to setting the "Requirements"**
1. git clone [this repository]
2. add properties
3. ./gradlew build
4. cd build/libs
5. java -jar [jar file]
