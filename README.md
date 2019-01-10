How to Deploy the App
=====================
1. Download or Clone the repository using following link:
https://github.com/agrawal-ank/computer-store.git
2. cd <path to>\computer-store\sales-mgmt
3. Buid using "mvn package"
4. Run using "java -jar target\sales-mgmt.1.0.jar


Application Usage:
==================
Application supports following commands:
  a. import <csv-file>
  b. export <file>
  c. exit

Command Usage:
==============
import <csv-file>
  - Loads csv data into the database.
  - Each line in csv-file represents one product item in following format:
	<ProductType>;<Amount>;series_number:<>;manufacturer:<>;price:<>;<ProductPropertyKey>:<ProductPropertyValue>

  parameter:
  csv-file: csv-file path


export <file>
  - export data from the database into the file in following tree structure:
	Computer
		Desktop
			HP
				[S/N=HD100, PRICE=10000, AMOUNT=10]
				[S/N=HD200, PRICE=20000, AMOUNT=20]
			Lenovo
				[S/N=LD100, PRICE=10000, AMOUNT=20]
				[S/N=LD200, PRICE=20000, AMOUNT=30]
		Nettop
			HP
				[S/N=HN100, PRICE=15000, AMOUNT=10]
				[S/N=HN200, PRICE=20000, AMOUNT=20]
			Dell
				[S/N=SN100, PRICE=20000, AMOUNT=20]
				[S/N=SN200, PRICE=25000, AMOUNT=30]
	Laptop
		14
			HP
				[S/N=HL14-100, PRICE=50000, AMOUNT=10]
				[S/N=HL14-100, PRICE=60000, AMOUNT=10]


  parameter:
  file: file path