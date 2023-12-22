# HiK-Server
Rest API server implemented with Spring boot.

## Code Organization
```
src
├── main
│   ├── java
│   │   └── HiK
│   │       └── HiKServer
│   │           ├── HiKServerApplication.java
│   │           ├── LearningContents
│   │           │   ├── controller
│   │           │   │   └── LearningApiController.java
│   │           │   ├── domain
│   │           │   │   └── LearningContent.java
│   │           │   ├── dto
│   │           │   │   ├── ChangeSentenceDto.java
│   │           │   │   ├── ChangeSentenceResponseDto.java
│   │           │   │   ├── LearningContentDto.java
│   │           │   │   ├── ReasonRequestDto.java
│   │           │   │   ├── ReasonResponseDto.java
│   │           │   │   └── SentenceListDto.java
│   │           │   ├── repository
│   │           │   │   └── LearningContentRepository.java
│   │           │   └── service
│   │           │       ├── GptPrompt_Learning.java
│   │           │       └── LearningContentService.java
│   │           ├── Login
│   │           │   ├── controller
│   │           │   │   └── LoginController.java
│   │           │   ├── domain
│   │           │   │   ├── GoogleOauth.java
│   │           │   │   ├── SocialOauth.java
│   │           │   │   └── UserResource.java
│   │           │   └── service
│   │           │       └── LoginService.java
│   │           ├── Translator
│   │           │   ├── controller
│   │           │   │   └── TranslationController.java
│   │           │   ├── domain
│   │           │   │   └── Sentence.java
│   │           │   ├── dto
│   │           │   │   └── TranslationForm.java
│   │           │   ├── repositroy
│   │           │   │   └── SentenceRepository.java
│   │           │   └── service
│   │           │       ├── ChatMessage.java
│   │           │       ├── GptPrompt.java
│   │           │       ├── PromptHandler.java
│   │           │       ├── S3UploadService.java
│   │           │       └── TranslationService.java
│   │           ├── User
│   │           │   ├── controller
│   │           │   │   └── UserApiController.java
│   │           │   ├── domain
│   │           │   │   └── User.java
│   │           │   ├── repository
│   │           │   │   └── UserRepository.java
│   │           │   └── service
│   │           │       └── UserService.java
│   │           └── config
│   │               ├── AwsConfig.java
│   │               └── WebConfig.java
│   └── resources
│       ├── static
│       └── templates
│           ├── articles
│           └── layouts
└── test
    └── java
        └── HiK
            └── HiKServer
                ├── HiKServerApplicationTests.java
                ├── domain
                │   └── domainRelationTest.java
                └── repository
                    ├── LearningContentsTest.java
                    └── UserRepositoryTest.java
```

### Pattern
- **MVC Architecture Pattern**
  
  There is a Controller layer that receives requests from clients (View) and a service layer (Model) that receives requests from the controller and performs business logic. Translator, Learning Contents, Login, and User related functions are all performed through the MVC architecture pattern, and a repository layer is implemented to implement methods to access the DB.

- **Chain of Responsibility**
  
  In the process of selecting a prompt to run the gpt API, it was necessary to branch the prompt for optimization by location and target. Using the Chain of Responsibility pattern, different prompts depending on the location were created using the abstract class PromptHandler class and each prompt inheriting the PromptHandler class. Handler classes and the handleRequest method were implemented and provided.

- **Object Oriented Programming**

  There are User, Sentence, and LearningContent entities. Entities are each encapsulated, and getters must be used to access them.

## Getting Started
#### Requirements
- Google Cloud Platform - Register Login Api and TTS Api
- OpenAI - Chat GPT Api Key
- Amazon EC2, RDS, S3
#### Prerequisites
- Spring boot 2.7.x
#### Step-by-Step Building Instruction
**You have to setting the "Requirements"**
1. git clone [this repository]
2. add properties
3. ./gradlew build
4. cd build/libs
5. java -jar [jar file]
