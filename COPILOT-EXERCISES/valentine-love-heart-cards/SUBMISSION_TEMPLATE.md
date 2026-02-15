


*This is a submission for the [GitHub Copilot CLI Challenge](https://dev.to/challenges/github-2026-01-21)*

## What I Built

**Valentine Love Heart Cards** — a Spring Boot web application that hides secret messages inside valentine card images using LSB steganography.

### The Concept
Send someone a beautiful valentine image that secretly contains a hidden love message. Only someone with the app can decode it. It's like digital invisible ink for the modern age.

### How It Works
- **Read Mode:** Upload a valentine card image → see the hidden message (if one exists)
- **Create Mode:** Write your secret message → select a background image → download a valentine card with your message steganographically embedded

The app uses **Least Significant Bit (LSB) steganography** — it encodes your message by subtly modifying the last bit of each RGB color channel in the image pixels. The changes are invisible to the human eye but can be decoded by the algorithm.

### Tech Stack (Simple by Design)
- **Java 1.8** + **Spring Boot 2.7.18**
- **Thymeleaf** templates (no JavaScript frameworks)
- **Vanilla CSS** with responsive heart-shaped layout (70% viewport, mobile-friendly)
- **Maven** single-JAR deployment (`java -jar` ready)

### Why It Matters
This project represents a **"Fast and Furious but Safe"** development approach — I initially attempted an over-ambitious 7-layer multi-module architecture but ran out of time. Instead of giving up, I pivoted to a clean, simplified single-JAR design and shipped a working product in hours, not days.

**Philosophy:** Start simple. Ship working software. Scale later.

---

## Demo

### Running the Application
```bash
# Build
mvn clean package

# Run
java -jar target/valentine-love-heart-cards-0.0.1-SNAPSHOT.jar

# Access
http://localhost:8080
```

### Application Flow

**Home Page (Read Mode):**
- Heart-shaped UI with bow button at center
- Click bow → upload a valentine card image
- App decodes any hidden message and displays it

**Create Page:**
- Heart-shaped UI with textarea for message input
- Select background image (PNG/BMP/GIF)
- Click "Create Card" → download valentine card with embedded message
- Share the image; recipient uploads it to reveal your secret message

**View Page:**
- Shows decoded message after upload
- States: message found / no message / error / empty

### Screenshots
_(Screenshots would go here showing the heart layout, bow button, message display, and create form)_

### Repository
📁 [GitHub Repository](https://github.com/YOUR_USERNAME/valentine-love-heart-cards)  
_(Update with actual link)_

---

## My Experience with GitHub Copilot CLI

### The Game-Changer: Agents.md

I started by creating an **`Agents.md`** document — a comprehensive "contract" that defined:
- ✅ Tech stack: Java 1.8, Spring Boot 2.7.18, NO frameworks
- ✅ Architecture rules: layer dependencies, module structure
- ✅ Prohibited technologies: TypeScript, Angular, React, npm, etc.
- ✅ Testing philosophy: separate test modules, no test code in production JARs

This single file transformed GitHub Copilot from a code generator into an **autonomous senior developer**. I attached `Agents.md` to every session, and Copilot made correct architectural decisions without micro-management.

### The Workflow: "Show Me What You Can Do"

**Step 1: Documentation First**
Copilot created 5 framework documents before any code:
- `PLAN.md` — phased execution roadmap
- `README.md` — project overview + quick start
- `DESIGN_SPEC.md` — architecture and API design
- `INSTRUCTIONS.md` — developer setup guide
- `COPILOT_RETROSPECTIVE.md` — AI collaboration reflection

These docs served as **persistent context** — even when I switched AI models, the project knowledge was preserved.

**Step 2: Full Requirements, Single Prompt**
I provided complete application specs once:
- 3 pages (home, view, create)
- Heart-shaped layout (70% viewport, responsive)
- LSB steganography (32-bit length header + UTF-8 message in RGB LSBs)
- Mode switch toggle, specific positioning

Then I said: **"Show me what you can do."**

**Step 3: Systematic Autonomous Build**
Copilot built the entire application layer-by-layer:
1. **Backend:** DTOs → SteganographyService → CardService → CardController (7 Java files)
2. **Frontend:** CSS (heart layout + responsive) → 3 Thymeleaf templates → JS
3. **Build:** Fixed `pom.xml` Tomcat scope issue, ran `mvn clean package`, DONE.

I didn't write a single line of code. Just reviewed, validated, and approved.

### The Results: 70% Time Savings

**Estimated Time Without AI:** 15-20 hours
- Steganography research + implementation: 4-5 hours
- Backend services + controllers: 4-5 hours
- CSS heart layout + responsive: 3-4 hours
- Templates + integration: 2-3 hours
- Testing + debugging: 2-3 hours

**Actual Time With AI:** ~5 hours (including false start)
- Over-ambitious first attempt: 3 hours (learning experience)
- Pivot + Agents.md: 30 minutes
- AI building complete app: 45 minutes
- Review + validation: 15 minutes

**Productivity Multiplier: 3-4x**

### What Copilot Did Exceptionally Well

1. **Algorithm Implementation:** Generated LSB steganography encode/decode logic perfectly on first try — complex bit manipulation, capacity validation, sanity checks all correct. This alone saved 3-4 hours.

2. **CSS Layout Engineering:** Created heart shape using `::before`/`::after` pseudo-elements, responsive breakpoints (768px, 480px), perfect centering, no scroll — would have taken hours of trial-and-error.

3. **Consistent Patterns:** Applied Java 1.8 POJO conventions (no Lombok, explicit getters/setters) across 7 files without drift.

4. **Build System Expertise:** Proactively identified `spring-boot-starter-tomcat` with `provided` scope would break `java -jar` execution and fixed it.

**Bugs introduced:** 0 runtime bugs. Only 1 minor build config fix.

### The Pivot Moment: When Things Go Wrong

My initial plan was a complex 7-layer multi-module Maven architecture with logging aggregator integration. After 3 hours, I realized I'd run out of time.

**Human decision:** Abort. Create new simplified project.

**Copilot's role:** Executed the pivot flawlessly. Once I said "single-JAR, fast and furious," Copilot built the simplified version in under an hour.

**Lesson learned:** AI won't manage your time. Humans must set realistic scope. But when you need to pivot fast, AI is your parachute.

### Key Takeaways for AI-Assisted Development

✅ **Create an Agents.md:** Upfront constraints = AI autonomy  
✅ **Documentation = AI memory:** Markdown files persist context better than chat history  
✅ **Systematic > Reactive:** "Backend first, frontend second" prevents integration chaos  
✅ **Trust but verify:** AI-generated steganography worked perfectly, but I still validated the logic  
✅ **Start simple:** Shipped working single-JAR >> abandoned complex multi-module

### Would I Use GitHub Copilot Again?

**Absolutely yes. 9/10 experience.**

For well-defined web applications with clear constraints, GitHub Copilot is a **3-4x productivity multiplier**. It transformed a multi-day project into a half-day sprint while maintaining code quality.

The future of Spring Boot development is agentic AI. This contest forced me to discover that.

---

**Project Repository:** [valentine-love-heart-cards](https://github.com/YOUR_USERNAME/valentine-love-heart-cards)  
**Build:** `mvn clean package`  
**Run:** `java -jar target/valentine-love-heart-cards-0.0.1-SNAPSHOT.jar`  
**Live:** `http://localhost:8080`

💘 Happy Valentine's Day! May your messages stay secret until the right person reads them.
