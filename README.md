# System Notifications

### Requirements
https://agenticaipoc.atlassian.net/jira/software/projects/NOT/boards/2/backlog

## System Components
### Message Ingestion
- Applications send notifications as JSON messages via RabbitMQ.
- Messages are validated and stored in the queue for processing.

### Processing Layer
- Notification Processor fetches messages from RabbitMQ.
- Deduplication, throttling, and priority queuing are applied.
- User preferences are retrieved from the User Preference Database.

### Notification Delivery
- Messages are sent via Email, SMS, and WhatsApp delivery services.
- Failed messages go through a Retry Queue.

### Logging & Monitoring
- Audit Logs track sent, failed, and retried messages.
- An Admin Dashboard (future) provides monitoring & analytics.

## System Architecture

![System Architecture](architecture.png)

Edit here : https://www.mermaidchart.com/app/projects/82ea3771-f8c8-433e-8dc4-a509d3e55e6b/diagrams/acf65cdf-6b8f-47ea-a17a-0ca27b456c20/version/v0.1/edit
