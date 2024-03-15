# HALLO Data pipelines

Hallo data is sourced from a variety of partners in the Salish Sea. These include hydrophones (maintained by commercial partners), First Nations organizations, non-profit organizations, academic, national and international partners, and federal, provincial and municipal entities. To source data from the various partners we have developed custom pipelines to collect and ingest their data.

## About

Each pipeline is self contained, customized to the data provider and collected on unique schedules. The data is collected into a unified view which is available to the Data Portal and serves the needs of other stakeholders.

The intake pipelines that we have integrated are:
- SMRU
- SIMRES (_pending non-technical administrativa_)
- JASCO (_pending non-technical administrativa_)
- Orcacast (_pending_)

Outgoing pipelines are:
- Data API (_internal; in progress_)
- ML API (_in progress_)
- Detection Models (_internal; in progress_)
- Movement Models (_pending_)

## Configure your credentials and secrets

The configuration (e.g. credentials, secrets) of each pipeline is specified in a [config folder](../sample_config/smru-pipeline) for each pipeline (e.g. `../config/smru-pipeline/*.yaml`).

