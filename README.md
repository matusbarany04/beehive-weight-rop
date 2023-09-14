# Beehive Weight

An application/tool designed to monitor the weight of beehives.

## Table of Contents
1. [Features](#features)
2. [Installation](#installation)
3. [Development](#development)

## Features

* Real-time weight monitoring.
* Alerts on weight discrepancies.
* Weight Prediction Generation.
* Customizable Dashboard

## Installation

```bash
# Clone the repo
git clone https://github.com/matusbarany04/beehive-weight-rop.git

# Navigate to the directory
cd beehive-weight-rop

# Install dependecies for svelte frontend
cd svelte/
npm i

# Start continuous frontend compilation
npm run java:dev
```

## Development
Before pushing to the repository run prettier 
```bash
# Run prettier format to format code and sort tailwind functions
cd svelte/
npm run prettier:format
```
