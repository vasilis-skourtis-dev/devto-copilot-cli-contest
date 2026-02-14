# 💌 Valentine Cards - Steganography Application

Send secret love messages hidden in beautiful images!

## 🎯 Overview

Valentine Cards is a Spring Boot web application that uses **LSB (Least Significant Bit) steganography** to hide secret messages inside PNG images. Create romantic cards, send them via email, and let recipients decode your hidden messages!

## ✨ Features

- 🔒 **Hide Secret Messages** - Use steganography to encode messages in images
- 📧 **Email Delivery** - Send cards directly to recipients via email
- 🔓 **Decode Messages** - Upload cards to reveal hidden messages
- ❤️ **Beautiful UI** - Animated heart cards with romantic design
- 📤 **Drag & Drop** - Easy file upload with drag-and-drop support
- 🎨 **Custom Backgrounds** - Use any image as a card background

## 🏗️ Architecture

This application follows a **strict 7-layer architecture** for clean separation of concerns:

```
Layer 01: valentine-cards-model-universal (Constants, Enums)
Layer 02: valentine-cards-model-application-dtos (DTOs)
Layer 05: valentine-cards-domain-services (Business Logic)
Layer 06: valentine-cards-application-ui (Controllers, Templates)
Layer 07: valentine-cards-application-web (Main App, Config)
```

## 🛠️ Technology Stack

- **Java**: 1.8
- **Spring Boot**: 2.7.18
- **Templating**: Thymeleaf
- **Frontend**: HTML5 + CSS (no frameworks!)
- **JavaScript**: Vanilla JS (minimal, for drag-and-drop)
- **Build Tool**: Maven
- **Deployment**: Embedded Tomcat (single JAR)

## 🚀 Quick Start

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Build & Run

```bash
# Navigate to the project directory
cd COPILOT-EXERCISES/valentine-cards

# Build the application
mvn clean package

# Run the application
java -jar valentine-cards-application-web/target/valentine-cards-application-web-0.0.1-SNAPSHOT.jar

# Or use Spring Boot Maven plugin
cd valentine-cards-application-web
mvn spring-boot:run
```

### Access the Application

Open your browser and navigate to:
```
http://localhost:8080
```

## 📖 How to Use

### Create a Valentine Card

1. Click the heart on the home page to open the card interface
2. Switch to **"Create a Card"** mode
3. Enter your secret message (up to 10,000 characters)
4. Provide the recipient's email address
5. Upload a background image (PNG or JPEG)
6. Click **"Create & Send Card"**
7. The app will encode your message and send it via email!

### Read a Valentine Card

1. Switch to **"Read a Card"** mode
2. Drag and drop the card image (or click to browse)
3. The hidden message will be revealed instantly!

## 🔧 Configuration

### Email Settings

Email sending is **disabled by default** for development. To enable:

1. Edit `application.properties`:
```properties
valentine.cards.email.enabled=true
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

2. For Gmail:
   - Go to Google Account Settings → Security → 2-Step Verification
   - Create an "App Password"
   - Use the app password in the configuration

### Supported Email Providers

- **Gmail**: `smtp.gmail.com:587`
- **Outlook**: `smtp.office365.com:587`
- **Yahoo**: `smtp.mail.yahoo.com:587`

## 🔬 How Steganography Works

### LSB (Least Significant Bit) Algorithm

The application uses the LSB steganography technique:

1. **Encoding**:
   - Message is converted to bytes
   - Each bit of the message is stored in the least significant bit of RGB pixel values
   - The change is imperceptible to the human eye
   
2. **Decoding**:
   - LSBs are extracted from each pixel
   - Bits are reassembled into bytes
   - Bytes are decoded back to the original message

### Capacity

For a 500×500 pixel PNG image:
- **Available bits**: 500 × 500 × 3 (RGB) = 750,000 bits
- **Capacity**: ~93 KB of hidden text
- **Typical valentine message**: 1-5 KB

### Security

- Messages are **hidden**, not encrypted
- Cannot be detected by visual inspection
- Use additional encryption for sensitive data

## 📁 Project Structure

```
valentine-cards/
├── valentine-cards-model-universal/
│   ├── CardConstants.java
│   ├── OperationStatus.java
│   └── CardType.java
│
├── valentine-cards-model-application-dtos/
│   ├── CardCreationRequest.java
│   ├── CardCreationResponse.java
│   └── CardReadResponse.java
│
├── valentine-cards-domain-services/
│   ├── SteganographyService.java (Core LSB algorithm)
│   ├── ImageProcessingService.java
│   └── EmailService.java
│
├── valentine-cards-application-ui/
│   ├── controllers/
│   │   ├── HomeController.java
│   │   └── CardController.java
│   └── templates/
│       ├── home.html
│       └── card.html
│
└── valentine-cards-application-web/
    ├── ValentineCardsApplication.java (Main class)
    ├── application.properties
    └── static/
        ├── css/styles.css (Beautiful animations!)
        └── js/card.js (Drag-and-drop logic)
```

## 🎨 UI Features

### Animated Heart

- Pure CSS heart shape
- Smooth hover animations
- Responsive design

### Drag & Drop Upload

- Visual feedback on drag-over
- File type validation
- Image preview before submission

### Beautiful Styling

- Romantic gradient backgrounds
- Smooth transitions and animations
- Mobile-responsive layout

## 🧪 Testing

### Manual Testing

1. **Create a card**:
   - Use any PNG/JPEG image
   - Write a message
   - Verify the generated image looks identical to the original

2. **Read a card**:
   - Upload the generated image
   - Verify the message is decoded correctly
   - Try uploading a regular image (should show "no message found")

3. **Email delivery**:
   - Configure email settings
   - Send a card to yourself
   - Verify email is received with attachment

## 🔍 Troubleshooting

### Build Issues

```bash
# Clean all modules and rebuild
mvn clean install -U
```

### Email Not Sending

- Check `valentine.cards.email.enabled=true`
- Verify SMTP credentials
- Check firewall/antivirus settings
- Review logs for detailed errors

### Image Upload Fails

- Ensure image is under 5MB
- Use PNG or JPEG format only
- Check file permissions

## 📝 API Endpoints

### Read Card
```
POST /api/cards/read
Content-Type: multipart/form-data
Parameter: image (file)

Response: CardReadResponse {
  message: string,
  status: OperationStatus,
  messageFound: boolean
}
```

### Create Card
```
POST /api/cards/create
Content-Type: multipart/form-data
Parameters:
  - message (string)
  - recipientEmail (string)
  - senderName (string, optional)
  - emailSubject (string, optional)
  - backgroundImage (file)

Response: CardCreationResponse {
  status: OperationStatus,
  message: string,
  emailSent: boolean,
  imagePreview: string (base64)
}
```

## 🎓 Learning Resources

### Steganography
- [LSB Steganography Explained](https://en.wikipedia.org/wiki/Steganography)
- Understanding image encoding techniques

### Spring Boot
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- Thymeleaf templating

## 🤝 Contributing

This is a learning project demonstrating:
- Clean architecture principles
- Steganography implementation
- Modern Java development practices
- Simple, maintainable code

## 📄 License

This project is created for educational purposes.

## 💝 Happy Valentine's Day!

Spread love with secret messages! ❤️

---

**Created with ❤️ by Valentine Cards Team**  
*February 2026*
