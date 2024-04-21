CREATE TABLE IF NOT EXISTS Owner(
    ownerid INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS Property(
    propertyid INT AUTO_INCREMENT PRIMARY KEY,
    address VARCHAR(255),
    status VARCHAR(255),
    price INT
);

CREATE TABLE IF NOT EXISTS Lease(
    leaseid INT AUTO_INCREMENT PRIMARY KEY,
    startdate VARCHAR(255),
    enddate VARCHAR(255),
    propertyid INT,
    ownerid INT,
    FOREIGN KEY (propertyId) REFERENCES Property(propertyid),
    FOREIGN KEY (ownerId) REFERENCES Owner(ownerid)
);
