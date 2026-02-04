# Logs Aggregator

A centralized log collection, processing, and analysis system for distributed applications.

## Overview

The Logs Aggregator provides a robust solution for collecting logs from multiple sources, processing them in real-time, and making them searchable and analyzable. Perfect for monitoring distributed systems and microservices architectures.

## Features

- 📥 Multi-source log collection
- 🔄 Real-time log processing
- 🔍 Full-text search capabilities
- 🏷️ Automatic log categorization and tagging
- 📊 Log analytics and insights
- 🚨 Alert and notification system
- 💾 Efficient storage with retention policies
- 🔐 Secure log transmission

## Getting Started

### Prerequisites

- Python 3.8+ or Node.js 16+
- Redis (for message queuing)
- Elasticsearch or similar (for log storage)

### Installation

```bash
cd logs-aggregator
pip install -r requirements.txt
# or
npm install
```

### Configuration

Create a configuration file or set environment variables:

```env
LOG_SOURCES=http://service1:5000,http://service2:5000
STORAGE_BACKEND=elasticsearch
ELASTICSEARCH_URL=http://localhost:9200
REDIS_URL=redis://localhost:6379
```

### Running

```bash
# Start the aggregator
python main.py
# or
npm start
```

## Architecture

```
┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│   Sources   │────▶│  Aggregator  │────▶│   Storage   │
└─────────────┘     └──────────────┘     └─────────────┘
                           │
                           ▼
                    ┌──────────────┐
                    │   Analytics  │
                    └──────────────┘
```

## Usage

### Sending Logs

```python
import logging
from log_client import LogClient

client = LogClient('http://localhost:8080')
client.send_log(level='INFO', message='Application started', metadata={...})
```

### Querying Logs

```bash
# Search logs
curl http://localhost:8080/api/logs?q=error&from=2024-01-01

# Get statistics
curl http://localhost:8080/api/stats
```

## Project Structure

```
logs-aggregator/
├── src/
│   ├── collectors/    # Log collection modules
│   ├── processors/    # Log processing pipeline
│   ├── storage/       # Storage adapters
│   └── api/           # REST API endpoints
├── tests/             # Test files
├── config/            # Configuration files
└── README.md          # This file
```

## Technologies

- Python/Node.js
- Redis for message queuing
- Elasticsearch for log storage
- FastAPI/Express for API
- Docker for containerization

## Performance

- Handles 10,000+ logs/second
- Sub-second search queries
- Automatic scaling capabilities

## Contributing

Please read the main repository's contributing guidelines.

## License

MIT
