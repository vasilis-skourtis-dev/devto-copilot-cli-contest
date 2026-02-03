# Documentation Builder App

An automated documentation generation, building, and publishing system for technical projects.

## Overview

The Documentation Builder App streamlines the process of creating and maintaining technical documentation. It automatically generates documentation from various sources including code comments, markdown files, API specifications, and more, then builds them into beautiful, searchable documentation sites.

## Features

- 📚 Multi-source documentation aggregation
- 🔄 Automatic documentation generation from code
- 🎨 Multiple theme support
- 🔍 Full-text search integration
- 📖 API documentation from OpenAPI/Swagger specs
- 🌳 Automatic table of contents generation
- 🔗 Cross-reference link validation
- 📱 Mobile-responsive output
- 🚀 Static site generation for fast hosting
- 🔄 Git integration for versioned docs
- 📊 Documentation coverage reports

## Getting Started

### Prerequisites

- Node.js (v16 or higher) or Python 3.8+
- Git (for version control integration)

### Installation

```bash
cd documentation-builder-app
npm install
# or
pip install -r requirements.txt
```

### Quick Start

```bash
# Initialize a new documentation project
npm run init
# or
python cli.py init

# Build documentation
npm run build
# or
python cli.py build

# Serve documentation locally
npm run serve
# or
python cli.py serve
```

## Configuration

Create a `docbuilder.config.json` in your project root:

```json
{
  "source": {
    "markdown": ["./docs/**/*.md"],
    "code": ["./src/**/*.{js,ts,py}"],
    "openapi": ["./api/openapi.yaml"]
  },
  "output": "./dist/docs",
  "theme": "default",
  "features": {
    "search": true,
    "api": true,
    "toc": true,
    "darkMode": true
  },
  "versioning": {
    "enabled": true,
    "versions": ["latest", "v2.0", "v1.0"]
  }
}
```

## Usage

### Generate from Code Comments

```javascript
/**
 * @api {get} /users/:id Get User
 * @apiName GetUser
 * @apiGroup User
 * @apiDescription Retrieves a user by ID
 */
function getUser(id) { }
```

### Write Markdown Docs

```markdown
# API Overview

## Authentication
All API requests require authentication...
```

### Build and Deploy

```bash
# Build static documentation
npm run build

# Deploy to GitHub Pages
npm run deploy:github

# Deploy to custom server
npm run deploy --target production
```

## Project Structure

```
documentation-builder-app/
├── src/
│   ├── parsers/         # Code and format parsers
│   ├── generators/      # Documentation generators
│   ├── themes/          # UI themes
│   ├── builders/        # Static site builders
│   └── plugins/         # Extension plugins
├── templates/           # Documentation templates
├── themes/              # Built-in themes
├── tests/              # Test files
├── cli.py              # Command-line interface
└── README.md           # This file
```

## Supported Input Formats

- **Code Comments**: JSDoc, Python docstrings, JavaDoc, etc.
- **Markdown**: CommonMark, GitHub Flavored Markdown
- **API Specs**: OpenAPI 3.0, Swagger 2.0, GraphQL schemas
- **Config Files**: JSON, YAML, TOML documentation
- **Notebooks**: Jupyter notebooks (.ipynb)

## Output Formats

- Static HTML site (default)
- PDF documents
- Single-page HTML
- Markdown (reformatted/structured)
- JSON (for custom processing)

## Themes

Available themes:
- `default` - Clean, modern design
- `dark` - Dark mode optimized
- `minimal` - Minimalist documentation
- `api` - API-focused layout
- `book` - Book-style documentation

## Plugins

Extend functionality with plugins:

```javascript
// Custom plugin example
module.exports = {
  name: 'custom-plugin',
  hooks: {
    beforeBuild: (config) => { },
    afterBuild: (result) => { }
  }
};
```

## CLI Commands

```bash
# Initialize new documentation project
docbuilder init

# Build documentation
docbuilder build [options]

# Watch and rebuild on changes
docbuilder watch

# Serve documentation locally
docbuilder serve [--port 3000]

# Validate documentation links
docbuilder validate

# Generate coverage report
docbuilder coverage

# Deploy to hosting
docbuilder deploy [--target <name>]
```

## Integration Examples

### GitHub Actions

```yaml
name: Build Documentation
on: [push]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - run: npm install
      - run: npm run build
      - run: npm run deploy:github
```

### Pre-commit Hook

```bash
#!/bin/bash
# Validate docs before commit
npm run validate
```

## Technologies

- Node.js / Python
- Markdown-it / CommonMark
- OpenAPI Parser
- Static Site Generator (Vite/Next.js)
- Full-text Search (Lunr.js / MeiliSearch)
- Syntax Highlighting (Prism / Highlight.js)

## Advanced Features

### Version Management

```bash
# Create new documentation version
docbuilder version create v2.0

# Switch between versions
docbuilder version switch v1.0
```

### Custom Templates

```html
<!-- templates/custom.html -->
<!DOCTYPE html>
<html>
  <head>
    <title>{{ title }}</title>
  </head>
  <body>
    {{{ content }}}
  </body>
</html>
```

### Search Configuration

```json
{
  "search": {
    "engine": "lunr",
    "fields": ["title", "content", "tags"],
    "boost": {
      "title": 10,
      "headings": 5
    }
  }
}
```

## Performance

- **Build Speed**: 1000+ pages in under 10 seconds
- **Search**: Sub-50ms query response
- **Output Size**: Optimized, minified assets
- **SEO**: Full static HTML for crawler optimization

## Contributing

Please read the main repository's contributing guidelines.

## License

MIT

## Support

For issues and questions:
- GitHub Issues
- Documentation: https://docs.example.com
- Community Forum: https://community.example.com
