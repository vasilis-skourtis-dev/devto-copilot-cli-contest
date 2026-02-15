# 💌 Valentine Cards - Design Specification

**Version:** 1.0  
**Last Updated:** February 15, 2026  
**Status:** Draft - Under Development

---

## 📋 Table of Contents

1. [Design Philosophy](#design-philosophy)
2. [User Experience Vision](#user-experience-vision)
3. [Component Architecture](#component-architecture)
4. [Data Flow & State Management](#data-flow--state-management)
5. [Component Specifications](#component-specifications)
6. [Technology Stack](#technology-stack)
7. [Responsive Design Strategy](#responsive-design-strategy)
8. [Visual Design System](#visual-design-system)
9. [Animation Guidelines](#animation-guidelines)
10. [Accessibility Requirements](#accessibility-requirements)

---

## 🎯 Design Philosophy

### Core Principles

1. **Power of Minimalism**
   - Maximum information conveyed in minimum space
   - No unnecessary screens or complexity
   - Like a joke: if you have to explain it, it's not good

2. **Real-World Metaphor**
   - Imitate opening a real heart-shaped envelope
   - Natural, intuitive interactions
   - Elegant, discreet functionality

3. **Component Composition**
   - Build basic components (Level 1)
   - Compose them into complex components (Level 2, 3)
   - Reusable, maintainable, troubleshootable

4. **State-Driven Architecture**
   - Explicit data flow
   - Clear component hierarchy
   - Parent components control child state

5. **Engineering Excellence**
   - Clean separation of concerns
   - HTML structure, CSS styling, JS behavior (separate files)
   - Div-oriented, flexbox-based layout
   - No inline styles or scripts

---

## 💝 User Experience Vision

### The Journey

```
┌─────────────────────────────────────────────────────────────┐
│                                                               │
│  STEP 1: Home Screen                                         │
│  ┌───────────────────────────────────────────────────────┐  │
│  │                                                         │  │
│  │           [Closed Heart Envelope]                      │  │
│  │                                                         │  │
│  │          Click/Tap to Open ❤️                          │  │
│  │                                                         │  │
│  └───────────────────────────────────────────────────────┘  │
│                         ↓                                     │
│                      (Opens)                                  │
│                         ↓                                     │
│  STEP 2: Opened Card Screen                                 │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  Mode Switch: [Read Card] | [Create Card]             │  │
│  │                                                         │  │
│  │  IF READ MODE:                                         │  │
│  │    ┌─────────────────────────────────────────────┐    │  │
│  │    │  📤 Upload/Drop Image to Decode             │    │  │
│  │    └─────────────────────────────────────────────┘    │  │
│  │         ↓                                              │  │
│  │    ┌─────────────────────────────────────────────┐    │  │
│  │    │  💌 Secret Message Revealed                 │    │  │
│  │    │  (With zoomable text + image frames)        │    │  │
│  │    └─────────────────────────────────────────────┘    │  │
│  │                                                         │  │
│  │  IF CREATE MODE:                                       │  │
│  │    ┌─────────────────────────────────────────────┐    │  │
│  │    │  4-Step Wizard                              │    │  │
│  │    │  Step 1: Write Message                      │    │  │
│  │    │  Step 2: Add Recipients                     │    │  │
│  │    │  Step 3: Upload Background Image            │    │  │
│  │    │  Step 4: Preview & Send                     │    │  │
│  │    └─────────────────────────────────────────────┘    │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Interaction Design

**Mobile-First Principles:**
- Touch-friendly: Minimum 48px touch targets
- Tap over double-click
- Swipe gestures for wizard navigation
- Responsive: Works on phones, tablets, desktops

**Cross-Platform:**
- Web browser on desktop
- Mobile browser (iOS Safari, Android Chrome)
- No app installation needed

---

## 🏗️ Component Architecture

### Hierarchy Overview

```
LEVEL 1: BASIC COMPONENTS (Atoms)
├── mode-switch          → Read/Create toggle
├── upload-area          → Drag-drop file upload
├── text-field           → Editable ↔ Read-only text
└── fragmented-area      → Styled background + inner dashed area

LEVEL 2: COMPOSITE COMPONENTS (Molecules)
├── mode-container       → Switch + conditional content
├── read-card            → Upload area for decoding
├── create-card          → Wizard wrapper
├── image-thumbnail      → Preview + download button
└── recipients-list      → Email fields + "Add More"

LEVEL 3: COMPLEX COMPOSITES (Organisms)
├── card-creation-wizard → 4-step creation flow
│   ├── Step 1: Message textarea
│   ├── Step 2: Recipients list (editable)
│   ├── Step 3: Upload background
│   └── Step 4: Preview + Send
│
└── card-display         → Opened card view
    ├── Center: Message (zoomable)
    ├── Left Wing: Image frame
    └── Right Wing: Image frame
```

---

## 📊 Data Flow & State Management

### State Tree

```javascript
AppState = {
  // Global Mode
  mode: "read" | "create",
  
  // Read Mode State
  readMode: {
    uploadedImage: File | null,
    decodedMessage: String | null,
    displayState: {
      message: String,
      leftImage: URL,
      rightImage: URL,
      isZoomed: boolean
    }
  },
  
  // Create Mode State
  createMode: {
    wizard: {
      currentStep: 1 | 2 | 3 | 4,
      
      // Step 1
      message: String,
      
      // Step 2
      recipients: [String],  // Array of email addresses
      
      // Step 3
      backgroundImage: File | null,
      
      // Step 4 (populated by backend response)
      generatedImage: Blob | null,
      previewUrl: String
    }
  }
}
```

### State Flow Rules

1. **Mode Switch** → Updates `AppState.mode` → Triggers conditional rendering
2. **Wizard Navigation** → Updates `AppState.createMode.wizard.currentStep`
3. **User Input** → Updates relevant state properties
4. **API Response** → Updates state → Re-render affected components
5. **Parent Controls Children** → State flows downward, events bubble upward

### Stateless Backend Principle

- **Frontend owns all state**
- **Backend is pure service layer** (encode, decode, send)
- **No server-side sessions**
- All data passed in requests (may increase traffic, but more secure)
- User responsible for successful completion

---

## 📦 Component Specifications

### Level 1: Basic Components

#### 1. Mode Switch (`mode-switch`)

**Purpose:** Toggle between Read and Create modes

**States:**
- `data-mode="read"` - Read mode active
- `data-mode="create"` - Create mode active

**Events:**
- `modechange` - Fired when mode changes
  - `event.detail.mode` → "read" | "create"

**Structure:**
```html
<div class="mode-switch" data-mode="read">
  <div class="mode-switch__slider"></div>
  <button class="mode-switch__option active" data-mode="read">Read</button>
  <button class="mode-switch__option" data-mode="create">Create</button>
</div>
```

**CSS File:** `mode-switch.css`  
**Prototype:** `prototypes/level1/mode-switch.html`

---

#### 2. Upload Area (`upload-area`)

**Purpose:** Drag-and-drop + click-to-browse file upload

**States:**
- `.drag-over` - File being dragged over
- `.has-file` - File selected
- `.loading` - Processing file
- `.error` - Validation error

**Events:**
- `fileselected` - File successfully selected
  - `event.detail.file` → File object
- `filecleared` - File removed

**Features:**
- Drag-and-drop support
- Click/tap to browse
- File type validation (PNG, JPEG)
- File size validation (max 5MB)
- Image preview
- Remove button

**Structure:**
```html
<div class="upload-area">
  <button class="upload-area__remove">✕</button>
  <div class="upload-area__content">
    <div class="upload-area__icon">📤</div>
    <p class="upload-area__text">Drop your image here</p>
    <button class="upload-area__button">Browse Files</button>
  </div>
  <img class="upload-area__preview" />
  <input type="file" class="upload-area__input" accept="image/*" />
</div>
```

**CSS File:** `upload-area.css`  
**Prototype:** `prototypes/level1/upload-area.html`

---

#### 3. Text Field (`text-field`)

**Purpose:** Toggleable between editable and read-only states

**States:**
- `data-editable="true"` - Editable mode
- `data-editable="false"` - Read-only mode
- `.error` - Validation error
- `.success` - Validation success

**Variants:**
- Input field (single line)
- Textarea (multi-line, with character counter)

**Features:**
- Toggle edit/read mode
- Validation states
- Character counter (textarea)
- Handwriting font option (for romantic messages)

**Structure:**
```html
<div class="text-field" data-editable="true">
  <label class="text-field__label">Field Label</label>
  <div class="text-field__input-wrapper">
    <input class="text-field__input" />
    <div class="text-field__display"></div>
    <button class="text-field__toggle">Toggle</button>
  </div>
  <div class="text-field__error-message"></div>
</div>
```

**CSS File:** `text-field.css`  
**Prototype:** `prototypes/level1/text-field.html`

---

#### 4. Fragmented Area (`fragmented-area`)

**Purpose:** Styled background container with inner dashed content area

**Variants:**
- `.fragmented-area--gradient` - Gradient background
- `.fragmented-area--image` - Image background with overlay
- `.fragmented-area--frost` - Glassmorphism effect
- `.fragmented-area--card-frame` - Double border frame
- `.fragmented-area--hearts` - Heart pattern decoration

**Features:**
- Decorative corner accents (optional)
- Background patterns
- Inner dashed content area
- Flexible content alignment

**Structure:**
```html
<div class="fragmented-area fragmented-area--gradient">
  <div class="fragmented-area__corner fragmented-area__corner--top-left"></div>
  <!-- More corners... -->
  <div class="fragmented-area__inner">
    <!-- Content here -->
  </div>
</div>
```

**CSS File:** `fragmented-area.css`  
**Prototype:** `prototypes/level1/fragmented-area.html`

---

### Level 2: Composite Components

#### 5. Mode Container (`mode-container`)

**Purpose:** Combines mode switch with conditional content rendering

**Composition:**
- 1× Mode Switch (Level 1)
- 2× Content slots (read-card, create-card)

**Behavior:**
- Mode switch controls which content is visible
- Only one mode active at a time
- Smooth transitions between modes

---

#### 6. Read Card Component (`read-card`)

**Purpose:** Upload and decode Valentine card

**Composition:**
- 1× Upload Area (Level 1)
- 1× Card Display (Level 3) - shown after decode

**Flow:**
1. User uploads image
2. Frontend validates
3. API call to `/api/card/decode`
4. Display decoded message in Card Display component

---

#### 7. Image Thumbnail (`image-thumbnail`)

**Purpose:** Preview image with download capability

**Features:**
- Thumbnail display
- Download icon/button
- Responsive sizing

**Use Cases:**
- Preview generated card (Step 4 of wizard)
- Verify before sending

---

#### 8. Recipients List (`recipients-list`)

**Purpose:** Manage one or more email recipients

**Composition:**
- N× Text Field components (email type)
- 1× "Add More" button

**States:**
- Editable mode (Step 2 of wizard)
- Read-only mode (Step 4 preview)

**Features:**
- Add/remove recipients
- Email validation
- Support for multiple lovers (spread the love! 😄)
- Always at least one recipient field

---

### Level 3: Complex Composites

#### 9. Card Creation Wizard (`card-creation-wizard`)

**Purpose:** 4-step process to create and send Valentine card

**Steps:**

**Step 1: Write Message**
- 1× Textarea (Level 1) with calligraphic font
- Character counter (max 10,000 chars)
- "Next" button

**Step 2: Add Recipients**
- 1× Recipients List (Level 2)
- Default: sender's own email (for archive)
- "Add More" to include multiple recipients
- "Next" button

**Step 3: Upload Background**
- 1× Upload Area (Level 1)
- Action URL: `/api/card/generate`
- Backend returns: Base64 encoded image with steganography
- Stores result in state
- "Next" button

**Step 4: Preview & Send**
- 1× Image Thumbnail (Level 2) - generated card preview
- 1× Recipients List (Level 2) - read-only mode
- "Send" button → `/api/card/send`
- Success: Show confirmation
- Error: Show error message, allow retry

**Navigation:**
- Linear progression (1 → 2 → 3 → 4)
- "Back" buttons to previous steps
- Swipe gestures on mobile
- Progress indicator (1/4, 2/4, etc.)

**State Persistence:**
- All steps maintain state
- Can go back without losing data
- Uses browser sessionStorage as backup

---

#### 10. Card Display Component (`card-display`)

**Purpose:** Display decoded message in opened envelope style

**Layout:**

```
┌────────────────────────────────────────────────┐
│                                                │
│  [Left Wing]   [Center Message]   [Right Wing]│
│   Image Frame    (Zoomable)       Image Frame │
│                                                │
└────────────────────────────────────────────────┘
```

**Composition:**
- 2× Fragmented Area (Level 1) - left/right image frames
- 1× Fragmented Area (Level 1) - center message display with zoom

**Features:**
- Zoomable message text
- Romantic frame styling
- Responsive: stacks vertically on mobile

**Desktop Layout:**
```css
.card-display {
  display: flex;
  gap: var(--space-lg);
}

.card-display__wing {
  flex: 0 0 25%;
}

.card-display__center {
  flex: 1;
}
```

**Mobile Layout:**
```css
@media (max-width: 768px) {
  .card-display {
    flex-direction: column;
  }
}
```

---

## 🎨 Visual Design System

### Color Palette

```css
--color-primary: #e63946;           /* Passionate Red */
--color-primary-light: #f48c94;     /* Soft Pink */
--color-primary-dark: #c1121f;      /* Deep Red */
--color-secondary: #f77f00;         /* Warm Orange */
--color-accent: #ffb3c1;            /* Blush Pink */
--color-background: #fff5f7;        /* Soft White-Pink */
```

### Typography

**Fonts:**
- **Handwriting:** 'Dancing Script' (for messages)
- **Body:** 'Quicksand' (for UI elements)
- **Elegant:** 'Cormorant Garamond' (for titles)

**Font Sizes:**
```css
--font-size-xs: 0.75rem;   /* 12px */
--font-size-sm: 0.875rem;  /* 14px */
--font-size-base: 1rem;    /* 16px */
--font-size-md: 1.125rem;  /* 18px */
--font-size-lg: 1.5rem;    /* 24px */
--font-size-xl: 2rem;      /* 32px */
--font-size-xxl: 3rem;     /* 48px */
```

### Spacing System (8px Base)

```css
--space-xs: 0.5rem;    /* 8px */
--space-sm: 1rem;      /* 16px */
--space-md: 1.5rem;    /* 24px */
--space-lg: 2rem;      /* 32px */
--space-xl: 3rem;      /* 48px */
--space-xxl: 4rem;     /* 64px */
```

### Shadows & Depth

```css
--shadow-sm: 0 2px 4px rgba(0, 0, 0, 0.1);
--shadow-md: 0 4px 12px rgba(0, 0, 0, 0.15);
--shadow-lg: 0 8px 24px rgba(0, 0, 0, 0.2);
--shadow-xl: 0 16px 48px rgba(0, 0, 0, 0.25);
```

---

## 🎬 Animation Guidelines

### Heart Opening Animation

**Concept:** Envelope-style opening

```css
.heart-closed {
  transform: rotateY(0deg);
  transition: transform 0.8s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

.heart-opened .wing-left {
  transform: rotateY(-50deg);
}

.heart-opened .wing-right {
  transform: rotateY(50deg);
}
```

### Wizard Step Transitions

**Approach:** Slide in/out

```css
.wizard-step {
  display: none;
  opacity: 0;
  transform: translateX(100%);
}

.wizard-step.active {
  display: block;
  animation: slideIn 0.3s ease forwards;
}

@keyframes slideIn {
  to {
    opacity: 1;
    transform: translateX(0);
  }
}
```

### Performance Guidelines

- Use `transform` and `opacity` for animations (GPU-accelerated)
- Avoid animating `width`, `height`, `left`, `right`
- Keep animations under 500ms for perceived speed
- Use `will-change` sparingly

---

## ♿ Accessibility Requirements

### Keyboard Navigation

- All interactive elements accessible via Tab
- Enter/Space to activate buttons
- Escape to close modals/overlays
- Arrow keys for wizard navigation

### Screen Readers

- Semantic HTML (`<button>`, `<input>`, `<label>`)
- ARIA labels where needed
- Alt text for images
- Status announcements for state changes

### Focus Management

```css
:focus-visible {
  outline: 3px solid var(--color-primary);
  outline-offset: 2px;
}
```

### Color Contrast

- Minimum WCAG AA (4.5:1 for normal text)
- Icon-only buttons must have labels

---

## 📱 Responsive Design Strategy

### Breakpoints

```css
/* Mobile-first approach */
--breakpoint-sm: 640px;   /* Phones */
--breakpoint-md: 768px;   /* Tablets */
--breakpoint-lg: 1024px;  /* Laptops */
--breakpoint-xl: 1280px;  /* Desktops */
```

### Touch Targets

- Minimum 48×48px on mobile
- Adequate spacing between interactive elements
- No reliance on hover states

### Layout Strategy

**Mobile:**
- Single column
- Vertical stacking
- Full-width components

**Tablet/Desktop:**
- Multi-column where appropriate
- Horizontal layouts
- Max-width containers for readability

---

## 🚀 Technology Stack

### Core Constraints (from Agents.md)

- **Java:** 1.8
- **Spring Boot:** 2.7.18
- **Templating:** Thymeleaf
- **Frontend:** HTML5 + CSS + Vanilla JavaScript (minimal)
- **Build Tool:** Maven
- **Deployment:** Embedded Tomcat (java -jar)

### Explicitly Avoided

- ❌ TypeScript
- ❌ Angular, React, Vue.js
- ❌ Any JavaScript frameworks
- ❌ npm/Node.js build tooling

### Frontend Architecture

**File Organization:**
```
static/
├── css/
│   ├── valentine-base.css       (Foundation)
│   ├── level1/
│   ├── level2/
│   └── level3/
├── js/
│   ├── valentine-state.js       (State management)
│   ├── valentine-components.js  (Component behaviors)
│   └── valentine-api.js         (Backend integration)
└── images/

templates/
├── index.html
├── fragments/
│   ├── level1/
│   ├── level2/
│   └── level3/
└── layouts/
```

---

## 🔄 Backend API Endpoints

### Simplified Stateless API

#### 1. Decode Card
```
POST /api/card/decode
Content-Type: multipart/form-data

Request:
  - image: File

Response:
  {
    "message": "Secret message...",
    "status": "SUCCESS" | "NO_MESSAGE_FOUND" | "ERROR"
  }
```

#### 2. Generate Card
```
POST /api/card/generate
Content-Type: multipart/form-data

Request:
  - message: String
  - backgroundImage: File

Response:
  {
    "imageBase64": "data:image/png;base64,...",
    "status": "SUCCESS" | "ERROR"
  }
```

#### 3. Send Card
```
POST /api/card/send
Content-Type: application/json

Request:
  {
    "recipients": ["email1@example.com", "email2@example.com"],
    "imageBase64": "data:image/png;base64,...",
    "subject": "Optional subject"
  }

Response:
  {
    "sent": true,
    "status": "SUCCESS" | "ERROR",
    "message": "Sent to 2 recipients"
  }
```

---

## 📝 Implementation Notes

### Current Status

✅ **Completed:**
- CSS Foundation & Variables
- Level 1 Components (4/4)
  - mode-switch
  - upload-area
  - text-field
  - fragmented-area
- Standalone HTML prototypes (ready for testing)

🚧 **In Progress:**
- Level 2 Components
- Level 3 Components

⏳ **Next Steps:**
- Create Level 2 & 3 component prototypes
- Add JavaScript state management
- Convert to Thymeleaf fragments
- Integrate with backend APIs
- Add heart opening animation

---

## 🎨 Design Assets & Layout Mockups

### Layout Structure (Provided by User)

#### **State 1: Closed Heart with Bow**
```
┌─────────────────────────────────────┐
│        Body Content (Pink)          │
├─────────────────────────────────────┤
│     Main Container (Blue/Purple)    │
├─────────────────────────────────────┤
│   ╔═══════════════════════════╗     │
│   ║  HeartEnvelope Component  ║     │
│   ║         (Red)             ║     │
│   ║                           ║     │
│   ║  ┌───────────────────┐    ║     │
│   ║  │  Bow Element      │    ║     │
│   ║  │  Container        │    ║     │
│   ║  │  (Magenta)        │    ║     │
│   ║  └───────────────────┘    ║     │
│   ║                           ║     │
│   ║  ┌───────────────────┐    ║     │
│   ║  │   Mode Switch     │    ║     │
│   ║  │   (Yellow)        │    ║     │
│   ║  └───────────────────┘    ║     │
│   ╚═══════════════════════════╝     │
├─────────────────────────────────────┤
│     Main Container (Blue/Purple)    │
├─────────────────────────────────────┤
│        Body Content (Pink)          │
└─────────────────────────────────────┘
```

#### **State 2: Opened Heart with Wings**
```
┌─────────────────────────────────────┐
│        Body Content (Pink)          │
├─────────────────────────────────────┤
│     Main Container (Blue/Purple)    │
├─────────────────────────────────────┤
│   ╔═══════════════════════════╗     │
│   ║  HeartEnvelope Component  ║     │
│   ║         (Red)             ║     │
│   ║  ┌────┬──────────┬────┐   ║     │
│   ║  │Left│   Main   │Right│  ║     │
│   ║  │Wing│   Area   │Wing │  ║     │
│   ║  │    │          │     │  ║     │
│   ║  │    │(Magenta) │     │  ║     │
│   ║  │    │          │     │  ║     │
│   ║  │(Orange)       │(Orange)║     │
│   ║  └────┴──────────┴────┘   ║     │
│   ║  ┌───────────────────┐    ║     │
│   ║  │   Mode Switch     │    ║     │
│   ║  │   (Yellow)        │    ║     │
│   ║  └───────────────────┘    ║     │
│   ╚═══════════════════════════╝     │
├─────────────────────────────────────┤
│     Main Container (Blue/Purple)    │
├─────────────────────────────────────┤
│        Body Content (Pink)          │
└─────────────────────────────────────┘
```

### Layout Colors (from mockups):
- **Body Content**: `#ffc0ff` (Pink)
- **Main Container**: `#b3b3ff` (Light Blue/Purple)
- **HeartEnvelope Component**: `#ff0000` (Red)
- **Bow Element / Main Area**: `#ff00ff` (Magenta)
- **Left/Right Wings**: `#ff8000` (Orange)
- **Mode Switch**: `#ffff99` (Light Yellow)

---

## 💭 Open Questions & Decisions Needed

1. **Heart Animation Style:**
   - [ ] Fold/unfold like paper?
   - [ ] Rotate like a book?
   - [ ] Scale + transform?
   - [ ] Other: ___________

2. **Wizard Navigation:**
   - [ ] Buttons only ("Next", "Back")
   - [ ] Swipe gestures only
   - [ ] Both buttons AND swipe

3. **Email Validation:**
   - [ ] Real-time (on type)
   - [ ] On blur (when leaving field)
   - [ ] On submit only

4. **Default Recipient:**
   - [ ] Include sender's email by default
   - [ ] Require manual addition
   - [ ] Ask via checkbox

5. **Error Handling:**
   - [ ] Inline messages
   - [ ] Modal popups
   - [ ] Toast notifications

---

## 📚 References

- [Agents.md](Agents.md) - Architecture rules & constraints
- [README.md](README.md) - Project overview
- [Prototypes](/prototypes/) - Standalone component demos

---

## 🔄 Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2026-02-15 | Initial design specification |

---

**Note:** This is a living document. Update it as design decisions are made and requirements evolve.
