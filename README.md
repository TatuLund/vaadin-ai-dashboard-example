# Vaadin AI Dashboard Example

A demo showing how to build a lightweight, BI-style dashboard with Vaadin's
AI components. Each widget — a data grid or a chart — embeds its own chat
that drives the underlying view in natural language: ask for a query, change
the visualization, drill into a slice, and the widget updates itself.

<img width="1110" height="911" alt="dashboard-example" src="https://github.com/user-attachments/assets/5af8b86d-8883-4492-b3f0-ba38ae0a76c9" />

## What's in it

- **Composable dashboard** — drag, resize, and arrange grid/chart widgets on
  a [`Dashboard`](https://vaadin.com/docs/latest/components/dashboard).
- **Per-widget AI chat** — each widget has a popover chat backed by an
  `AIOrchestrator` that drives a `GridAIController` or `ChartAIController`.
- **In-memory H2 database** — seeded with a dozen demo tables (sales,
  employees, products, stocks, project tasks, org chart, energy flow,
  traffic heatmap, budget, sales pipeline, KPIs, expenses) covering the
  data shapes for most chart types.
- **Save/restore state** — snapshot dashboard layout, widget state, and
  chat history into the Vaadin session.
- **Pluggable LLM** — currently wired to OpenAI via LangChain4J; swap the
  provider in `DashboardView` to use a different model.

## Prerequisites

- Java 21
- A Vaadin Pro/Trial license
- An OpenAI API key in `OPENAI_API_KEY`

## Run

```bash
export OPENAI_API_KEY=sk-...
./mvnw
```

The dashboard is served at <http://localhost:8080/dashboard>. Add a grid or
chart widget from the toolbar, click the chat icon on a widget, and ask
something like _"show monthly revenue by region"_ or _"top 5 products by
units sold"_.

## Project layout

```
src/main/java/com/example/
├── Application.java                 Spring Boot entry point
├── InMemoryDatabaseProvider.java    H2-backed DatabaseProvider
├── DemoDataInitializer.java         Schema + seed data
└── views/
    ├── DashboardView.java           Top-level @Route("dashboard")
    ├── AIDashboardWidget.java       Grid/chart widget + orchestrator
    └── ChatLayouts.java             Chat layout factory
```

## Example prompts to test

Show monthly revenue broken down by region as a smooth line chart with data labels, a legend at the bottom, and a title 'Regional Revenue Trends'

Show a stacked column chart of total revenue per month, with one stack segment per region, data labels on each segment showing the dollar value, and the y-axis labeled 'Revenue ($)

Show ACME stock prices from the stock_prices table as a candlestick chart with dates on the x-axis, and add a secondary y-axis with a column series showing the trading volume

Show website traffic as a heatmap with days of the week (Monday through Friday) on the y-axis and hours (9-16) on the x-axis, using a blue-to-red color gradient, with the value shown in each cell's tooltip

Show monthly revenue by region as a 3D column chart with depth, tilted 15 degrees on alpha and 25 on beta, a light gray bottom frame panel, and the title '3D Revenue Overview'

Show monthly revenue by region as a column chart with the title 'Revenue by Region'
-> Change the South series to a spline line, make it dashed with red color, and add circle markers with radius 6

## Notes

- Vaadin's AI components are experimental — enabled via
  `src/main/resources/vaadin-featureflags.properties`.
- The OpenAI model name is hardcoded in `DashboardView`; change it there.
