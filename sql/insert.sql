INSERT OR IGNORE INTO Customer (customerId, name, email)
VALUES
('c1', 'Jon', 'jon@email.com'),
('c2', 'Anna', 'anna@email.com');

INSERT OR IGNORE INTO Hotel (hotelId, name, location, allowsPets)
VALUES
('nordic-light-hotel', 'Nordic Light Hotel', 'Reykjavik', 1),
('harbor-stay', 'Harbor Stay', 'Akureyri', 0),
('lava-suites', 'Lava Suites', 'Selfoss', 1),
('northern-peaks-resort', 'Northern Peaks Resort', 'Akureyri', 1);

INSERT OR IGNORE INTO Room
(roomId, hotelName, roomNumber, isBooked, roomType, hasBalcony, numberOfBeds, hasKitchen, pricePerDay)
VALUES
('nordic-light-hotel-101', 'Nordic Light Hotel', 101, 0, 'Single', 0, 1, 0, 15000),
('nordic-light-hotel-102', 'Nordic Light Hotel', 102, 0, 'Double', 1, 2, 0, 20000),
('nordic-light-hotel-201', 'Nordic Light Hotel', 201, 1, 'Suite', 1, 3, 1, 35000),
('harbor-stay-10', 'Harbor Stay', 10, 0, 'Double', 1, 2, 0, 18000),
('harbor-stay-11', 'Harbor Stay', 11, 0, 'Family', 0, 4, 1, 26000),
('harbor-stay-12', 'Harbor Stay', 12, 1, 'Single', 0, 1, 0, 14000),
('lava-suites-1', 'Lava Suites', 1, 0, 'Studio', 0, 2, 1, 22000),
('lava-suites-2', 'Lava Suites', 2, 0, 'Deluxe', 1, 2, 1, 28000),
('lava-suites-3', 'Lava Suites', 3, 0, 'Family', 1, 5, 1, 42000),
('northern-peaks-resort-301', 'Northern Peaks Resort', 301, 0, 'Double', 1, 2, 0, 24000),
('northern-peaks-resort-302', 'Northern Peaks Resort', 302, 0, 'Deluxe', 1, 3, 1, 31000),
('northern-peaks-resort-401', 'Northern Peaks Resort', 401, 1, 'Suite', 1, 4, 1, 45000);
