[![codecov](https://codecov.io/github/AY2526S1-CS2103T-W08-1/tp/graph/badge.svg?token=SHFCVBT8YC)](https://codecov.io/github/AY2526S1-CS2103T-W08-1/tp)
[![Java CI](https://github.com/AY2526S1-CS2103T-W08-1/tp/actions/workflows/gradle.yml/badge.svg)](https://github.com/AY2526S1-CS2103T-W08-1/tp/actions/workflows/gradle.yml)

# PressPal

## Table of Contents
- [About](#about)
- [UI](#ui)
- [Features](#features)
- [Commands](#commands)
- [Acknowledge Source](#acknowledge-source)

## About
PressPal is designed to streamline contact and interview management for breaking news reporters. Traditional address books and note-taking tools are not optimized for the high-speed, high-pressure environment of news reporting, where reporters must manage hundreds of contacts across multiple sources (phones, emails, notebooks).

The goal of PressPal is to:
- Provide reporters with a single command-driven system for organizing and retrieving contacts quickly.
- Enable fast entry and retrieval of context (interview history, organization, role, notes) while working on multiple stories simultaneously.
- Support the entire lifecycle of a contact — from initial outreach, to active follow-up, to archiving once a story concludes.

Ultimately, PressPal aims to reduce cognitive load for reporters, allowing them to focus on storytelling while ensuring no lead, contact, or follow-up is lost.

## UI
PressPal features a clean and intuitive user interface designed for ease of use and seamless navigation.
![Ui](docs/images/Ui.png)

## Features
1. Add Contacts
2. Delete a Contact
3. Add Interview to Contact
4. Delete Interview from Contact
5. Find Contact
6. List interviews for a Contact

## Commands
### Add Contact
Create a new contact with essential details.

`add n/NAME p/PHONE e/EMAIL o/ORGANIZATION r/ROLE [c/CATEGORY]`

### Delete Contact
Delete an existing contact

`delete PERSON_INDEX`

### Add Interview to Contact
Attach an interview date to an existing contact

`addInterview i/PERSON_INDEX h/HEADER d/DATE t/TIME l/LOCATION`

### Delete Interview from Contact
Remove an existing interview from a contact

`deleteInterview PERSON_INDEX INTERVIEW_INDEX`

### Find Contact
Find existing contacts by searching name

`find n/NAME`

### List interviews for a Contact
List all interviews for a contact

`listInterview i/PERSON_INDEX`

## Acknowledge Source
This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).
