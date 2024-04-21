-- Inserting data into Owner table
INSERT INTO Owner (name) VALUES ('John Doe');
INSERT INTO Owner (name) VALUES ('Jane Smith');

-- Inserting data into Property table
INSERT INTO Property (address, status, price) VALUES ('1234 Elm St', 'Not-Available',1200);
INSERT INTO Property (address, status, price) VALUES ('5678 Oak St', 'Not-Available',15000);

-- Inserting data into Lease table
INSERT INTO Lease (startDate, endDate, propertyid, ownerid) VALUES ('2022-01-01', '2022-12-31', 1, 1);
INSERT INTO Lease (startDate, endDate, propertyid, ownerid) VALUES ('2022-02-01', '2023-01-31', 2, 2);
