# Entity Relationship Diagram

```mermaid
erDiagram
    author ||--o{ book : "writes"

    author {
        int id PK
        varchar first_name
        varchar last_name
    }

    book {
        int id PK
        int aisle_number
        int author_id FK
        varchar title
        varchar isbn UK
    }
```
