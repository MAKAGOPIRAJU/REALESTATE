-- Inserting data into Owner table
INSERT INTO Owner (name)
SELECT 'John Doe'
WHERE NOT EXISTS (SELECT 1 FROM Owner WHERE name = 'John Doe');

INSERT INTO Owner (name)
SELECT 'Jane Smith'
WHERE NOT EXISTS (SELECT 1 FROM Owner WHERE name = 'Jane Smith');

-- Inserting data into Property table
INSERT INTO Property (address, status, price)
SELECT '1234 Elm St', 'Not-Available', 1200
WHERE NOT EXISTS (SELECT 1 FROM Property WHERE address = '1234 Elm St');

INSERT INTO Property (address, status, price)
SELECT '5678 Oak St', 'Not-Available', 15000
WHERE NOT EXISTS (SELECT 1 FROM Property WHERE address = '5678 Oak St');

INSERT INTO Property (address, status, price)
SELECT '123 Main St', 'Available', 20000
WHERE NOT EXISTS (SELECT 1 FROM Property WHERE address = '123 Main St');

-- Inserting data into Lease table
INSERT INTO Lease (startdate, enddate, propertyid, ownerid)
SELECT '2022-01-01', '2022-12-31', 1, 1
WHERE NOT EXISTS (SELECT 1 FROM Lease WHERE propertyid = 1 AND ownerid = 1);

INSERT INTO Lease (startdate, enddate, propertyid, ownerid)
SELECT '2022-02-01', '2023-01-31', 2, 2
WHERE NOT EXISTS (SELECT 1 FROM Lease WHERE propertyid = 2 AND ownerid = 2);
