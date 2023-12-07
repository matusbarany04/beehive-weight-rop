# Beehive Weight

BuzzysBees is a technology composed of a web interface and a multifunctional monitoring device used for recording data from bee hives. The device measures basic parameters (such as weight, temperature, air humidity...), responds to them, and automatically forwards the acquired data to a server. Beekeepers can view this data on a website, as well as adjust the settings of individual devices.

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

## Generating Svelte Docs
From time to time you can build new docs 
```bash
# Run this to parse .svelte files to parseble form
cd svelte/
npm run docs:prepare

# After that you can build .md file
npm run docs:build:md

#or build whole documentation page
npm run docs:build
```
