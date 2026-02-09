# Postcards

A creative digital postcard generation and sharing platform.

## Overview

Postcards is a delightful application that allows users to create, customize, and send beautiful digital postcards. Whether it's for holidays, special occasions, or just saying hello, Postcards makes it easy and fun.

## Features

- 🎨 Rich template library
- ✏️ Custom text and fonts
- 🖼️ Image upload and editing
- 🎭 Filters and effects
- 📧 Multiple sharing options (email, social media)
- 💾 Save and manage postcard collections
- 📱 Mobile-friendly interface
- 🌍 Multi-language support

## Getting Started

### Prerequisites

- Node.js (v16 or higher) or Python 3.8+
- npm/yarn or pip

### Installation

```bash
cd postcards
npm install
# or
pip install -r requirements.txt
```

### Development

```bash
npm run dev
# or
python app.py
```

Access the application at `http://localhost:3000`

### Build for Production

```bash
npm run build
# or
python -m build
```

## Usage

1. **Choose a Template**: Browse through the template gallery
2. **Customize**: Add your text, images, and personal touches
3. **Preview**: See how your postcard looks
4. **Send**: Share via email or social media
5. **Save**: Keep your creations in your personal collection

## Project Structure

```
postcards/
├── src/
│   ├── templates/     # Postcard templates
│   ├── components/    # UI components
│   ├── editor/        # Postcard editor
│   └── services/      # Email and sharing services
├── public/
│   └── assets/        # Images and static files
├── tests/             # Test files
└── README.md          # This file
```

## API Endpoints

```
POST   /api/postcards          # Create a new postcard
GET    /api/postcards/:id      # Get a postcard
PUT    /api/postcards/:id      # Update a postcard
DELETE /api/postcards/:id      # Delete a postcard
POST   /api/postcards/:id/send # Send a postcard
GET    /api/templates          # List available templates
```

## Configuration

```env
SMTP_HOST=smtp.example.com
SMTP_PORT=587
SMTP_USER=noreply@example.com
SMTP_PASS=password
UPLOAD_MAX_SIZE=5242880
ALLOWED_IMAGE_TYPES=jpg,png,gif
```

## Technologies

- React/Vue for frontend
- Node.js/Python for backend
- Canvas API for image editing
- SendGrid/Mailgun for email delivery
- AWS S3/Cloudinary for image storage

## Templates

Templates are stored in JSON format and can be easily customized:

```json
{
  "id": "holiday-2024",
  "name": "Holiday Greetings",
  "category": "holidays",
  "layout": {...},
  "defaultText": "Happy Holidays!"
}
```

## Contributing

We welcome contributions! Please read the main repository's contributing guidelines.

## License

MIT
