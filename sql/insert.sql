INSERT INTO Customer (customerId, name, email)
VALUES
('c1', 'Jon', 'jon@email.com'),
('c2', 'Anna', 'anna@email.com');

INSERT OR IGNORE INTO Room
(roomId, hotelName, roomNumber, isBooked, roomType, hasBalcony, numberOfBeds, hasKitchen, pricePerDay)
VALUES
('nordic-light-hotel-101', 'Nordic Light Hotel', 101, 0, 'Single', 0, 1, 0, 15000),
('nordic-light-hotel-102', 'Nordic Light Hotel', 102, 0, 'Double', 1, 2, 0, 20000),
('nordic-light-hotel-201', 'Nordic Light Hotel', 201, 1, 'Suite', 1, 3, 1, 35000),
('harbor-stay-10', 'Harbor Stay', 10, 0, 'Double', 1, 2, 0, 18000);