# JPA-Practice
This project documents the gradual development of a Java Persistence API(JPA) application as I learn its concepts and functionalities.

## Project Goals
- To learn and apply JPA concepts.
- To document the usage and implementation details of various JPA features.

## Project Structure
This repository consists of multiple projects in several sub-repositorries, each representing an independent practice project.

## Sub-Projects
### Entity-Mapping[^1]
The proejct Entity-Mapping is designed to learn the fundamental concepts of JPA. It explores the basic approaches to handling EntityManager and Transaction.

### Relation-Mapping01[^1]
The project Realation-Mapping01 is designed to learn the basic concepts of entity mappping in JPA. It serves as a practical example of various basic concepts in JPA including :
- @ManyToOne
- @JoinColumn(name = "...")
- @OneToMany(mappedBy = "...")
- @Enumberated(EnumType.String)

### Relation-Mapping0[^1]
The proejct Relation-Mapping02 is designed to learn the various mapping types in JPA. Threre are 4 types of mapping such as
- @OneToOne
- @OneToMany
- @ManyToOne
- @ManyToMany
In this project these all kinds of mapping are used.

[^1]: This project is an example code in the lecture [<Java ORM Standard JPA Programming - Basic>](https://www.inflearn.com/course/ORM-JPA-Basic)
