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
PressPal is designed to streamline contact and interview management for breaking news reporters. Traditional organisation books and note-taking tools are not optimized for the high-speed, high-pressure environment of news reporting, where reporters must manage hundreds of contacts across multiple sources (phones, emails, notebooks).

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
3. Create Category
4. Add Contact to Category
5. Remove Contact from Category
6. Add Interview to Contact
7. Delete Interview from Contact
8. Search Contact

## Commands
### Add Contact
Create a new contact with essential details.

`contact add n/NAME p/PHONE e/EMAIL o/ORGANIZATION r/ROLE c/CATEGORY(optional)`

### Delete Contact
Delete an existing contact

`contact delete n/NAME`

### Create Category
Create a new category to group contacts and interactions

`category create c/CATEGORY_NAME`

### Add Contact to Category
Group existing contacts to a category

`category add n/CONTACT_NAME c/CATEGORY_NAME`

### Remove Contact from Category
Remove contact from a category that it is already in

`category remove n/CONTACT_NAME c/CATEGORY_NAME`

### Add Interview to Contact
Attach an interview date to an existing contact

`interview add n/NAME h/HEADER d/DATE t/TIME`

### Delete Interview from Contact
Remove an existing interview from a contact

`interview delete n/NAME d/DATE t/TIME`

### Search Contact
Find existing contacts by searching name

`search n/NAME`

## Acknowledge Source
This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).
