# Library-Management-System
A Library Management System (LMS) is a software solution designed to efficiently manage the various operations and resources of a library. It encompasses a range of functionalities that aid 
librarians and library staff in cataloging, organizing, lending, and tracking library materials, as well as providing services to library patrons. 

Here's a concise overview of the key features and components of a typical Library Management System:
1. User Authentication and Authorization: Users, including librarians and students, can log in with appropriate credentials.
   Role-based authorization ensures that users can access only the functionalities they are authorized to use.
2. The system allows librarians to add, update books.Information such as title, author, publication date, quantity and genre can be stored.
3. Student information is stored, including details like name, age information, and borrowing history. Librarians can register new students, modify existing records.
4. Students can borrow materials by checking them out, and librarians can process returns by checking materials back in.
   The system keeps track of due dates, late fees, and the availability of items.
5. A search functionality enables users to easily find and locate materials using filters such as title, author, genre.
6. The system calculates and manages late fees and fines for overdue items. Librarians can review and update fine records.


#### Redis Configuration: 
###### If you do not have redis in your system ,then there will be error in accessing some APIs.
###### As part of configuration please ensure you have Redis in your system and also start the server before accessing the APIs.
###### Command for starting redis server : sudo service redis-server start
###### For monitoring the cache hit two commands ->
      1. redis-cli
      2. monitor

##### POSTMAN COLLECTION IS ALSO ADDED FOR REFERENCE
