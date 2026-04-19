CREATE TABLE Customer (
    customerId TEXT PRIMARY KEY,
    name TEXT,
    email TEXT
);

CREATE TABLE Room (
    roomId TEXT PRIMARY KEY,
    roomNumber INTEGER,
    pricePerDay REAL
);

CREATE TABLE Booking (
    bookingId TEXT PRIMARY KEY,
    customerId TEXT,
    roomId TEXT,
    checkInDate TEXT,
    checkOutDate TEXT,
    guests INTEGER,
    lateCheckout INTEGER,
    price REAL,
    FOREIGN KEY (customerId) REFERENCES Customer(customerId),
    FOREIGN KEY (roomId) REFERENCES Room(roomId)
);