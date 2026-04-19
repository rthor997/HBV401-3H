CREATE TABLE Customer (
    customerId TEXT PRIMARY KEY,
    name TEXT,
    email TEXT
);

CREATE TABLE IF NOT EXISTS Room (
    roomId TEXT PRIMARY KEY,
    hotelName TEXT,
    roomNumber INTEGER,
    isBooked INTEGER,
    roomType TEXT,
    hasBalcony INTEGER,
    numberOfBeds INTEGER,
    hasKitchen INTEGER,
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