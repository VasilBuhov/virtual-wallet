/*
Navicat SQL Server Data Transfer

Source Server         : Local DBM
Source Server Version : 150000
Source Host           : localhost:1433
Source Database       : wallet
Source Schema         : dbo

Target Server Type    : SQL Server
Target Server Version : 150000
File Encoding         : 65001

Date: 2023-09-01 20:01:08
*/


-- ----------------------------
-- Table structure for cards
-- ----------------------------
DROP TABLE [dbo].[cards]
GO
CREATE TABLE [dbo].[cards] (
[id] int NOT NULL ,
[card_number] varchar(16) NOT NULL ,
[check_number] int NULL ,
[card_holder] int NULL ,
[expiration_date] date NULL ,
[status_deleted] int NOT NULL DEFAULT ((0)) ,
[name] varchar(1) NULL 
)


GO

-- ----------------------------
-- Records of cards
-- ----------------------------
INSERT INTO [dbo].[cards] ([id], [card_number], [check_number], [card_holder], [expiration_date], [status_deleted], [name]) VALUES (N'2', N'1234567890123456', N'999', N'3', N'2025-12-31', N'0', null)
GO
GO
INSERT INTO [dbo].[cards] ([id], [card_number], [check_number], [card_holder], [expiration_date], [status_deleted], [name]) VALUES (N'3', N'9876543210987654', N'456', N'3', N'2024-10-15', N'0', null)
GO
GO

-- ----------------------------
-- Table structure for interest_rates
-- ----------------------------
DROP TABLE [dbo].[interest_rates]
GO
CREATE TABLE [dbo].[interest_rates] (
[id] int NOT NULL IDENTITY(1,1) ,
[interest] float(53) NULL ,
[timestamp] datetime NULL 
)


GO
DBCC CHECKIDENT(N'[dbo].[interest_rates]', RESEED, 2)
GO

-- ----------------------------
-- Records of interest_rates
-- ----------------------------
SET IDENTITY_INSERT [dbo].[interest_rates] ON
GO
INSERT INTO [dbo].[interest_rates] ([id], [interest], [timestamp]) VALUES (N'2', N'0.02', N'2023-08-25 01:27:11.117')
GO
GO
SET IDENTITY_INSERT [dbo].[interest_rates] OFF
GO

-- ----------------------------
-- Table structure for savings_wallets
-- ----------------------------
DROP TABLE [dbo].[savings_wallets]
GO
CREATE TABLE [dbo].[savings_wallets] (
[id] int NOT NULL IDENTITY(1,1) ,
[owner] int NOT NULL ,
[deposit] decimal(18) NULL ,
[duration_months] int NOT NULL ,
[from_wallet] int NOT NULL ,
[interest_rate] float(53) NULL ,
[end_date] datetime NULL ,
[interest_reward] decimal(18) NULL ,
[is_deleted] int NOT NULL DEFAULT ((0)) 
)


GO

-- ----------------------------
-- Records of savings_wallets
-- ----------------------------
SET IDENTITY_INSERT [dbo].[savings_wallets] ON
GO
SET IDENTITY_INSERT [dbo].[savings_wallets] OFF
GO

-- ----------------------------
-- Table structure for transactions
-- ----------------------------
DROP TABLE [dbo].[transactions]
GO
CREATE TABLE [dbo].[transactions] (
[id] int NOT NULL IDENTITY(1,1) ,
[type] int NULL ,
[amount] int NULL ,
[sender_id] int NULL ,
[recipient_id] int NULL ,
[status] varchar(10) NOT NULL ,
[date] datetime NOT NULL ,
[transaction_description] varchar(200) NOT NULL ,
[wallet_id] int NULL 
)


GO
DBCC CHECKIDENT(N'[dbo].[transactions]', RESEED, 41)
GO

-- ----------------------------
-- Records of transactions
-- ----------------------------
SET IDENTITY_INSERT [dbo].[transactions] ON
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'12', N'1', N'100', N'4', N'2', N'Completed', N'2023-08-14 10:00:00.000', N'Payment for goods', N'2')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'13', N'0', N'50', N'3', N'4', N'Completed', N'2023-08-14 11:30:00.000', N'Transfer to friend', N'2')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'14', N'1', N'200', N'2', N'6', N'Pending', N'2023-08-14 12:45:00.000', N'Online purchase', N'2')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'15', N'0', N'75', N'4', N'3', N'Completed', N'2023-08-14 14:20:00.000', N'Repayment', N'2')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'16', N'1', N'100', N'5', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'1')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'17', N'1', N'100', N'5', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'5')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'18', N'1', N'100', N'5', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'5')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'19', N'1', N'100', N'2', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'5')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'20', N'1', N'100', N'2', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'5')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'21', N'1', N'100', N'2', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'5')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'22', N'1', N'100', N'2', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'5')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'23', N'1', N'100', N'2', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'4')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'24', N'1', N'100', N'3', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'4')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'26', N'1', N'100', N'3', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'6')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'27', N'1', N'100', N'3', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'4')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'28', N'1', N'100', N'3', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'4')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'29', N'1', N'100', N'3', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'4')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'30', N'1', N'100', N'3', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'4')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'31', N'1', N'100', N'3', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'4')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'32', N'1', N'100', N'3', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'4')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'33', N'1', N'100', N'3', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'4')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'34', N'1', N'100', N'3', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'4')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'35', N'1', N'100', N'3', N'2', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'4')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'36', N'1', N'350', N'3', N'4', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'4')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'37', N'1', N'350', N'3', N'4', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'4')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'38', N'1', N'350', N'3', N'4', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'2')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'39', N'1', N'350', N'3', N'4', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'2')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'40', N'1', N'350', N'3', N'4', N'PENDING', N'2023-08-14 10:00:00.000', N'Payment for goods', N'2')
GO
GO
INSERT INTO [dbo].[transactions] ([id], [type], [amount], [sender_id], [recipient_id], [status], [date], [transaction_description], [wallet_id]) VALUES (N'41', N'1', N'350', N'3', N'4', N'PENDING', N'2023-08-29 13:40:29.847', N'Payment for goods', N'2')
GO
GO
SET IDENTITY_INSERT [dbo].[transactions] OFF
GO

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE [dbo].[users]
GO
CREATE TABLE [dbo].[users] (
[id] int NOT NULL IDENTITY(1,1) ,
[username] varchar(255) NOT NULL ,
[first_name] varchar(50) NULL ,
[last_name] varchar(50) NULL ,
[email] varchar(75) NOT NULL ,
[password] varchar(50) NOT NULL ,
[user_level] int NULL ,
[verified] int NULL ,
[verification_code] varchar(64) NULL ,
[enabled] bit NULL ,
[profile_picture] image NULL ,
[phone] varchar(50) NULL ,
[status_deleted] bit NULL 
)


GO
DBCC CHECKIDENT(N'[dbo].[users]', RESEED, 18)
GO

-- ----------------------------
-- Records of users
-- ----------------------------
SET IDENTITY_INSERT [dbo].[users] ON
GO
INSERT INTO [dbo].[users] ([id], [username], [first_name], [last_name], [email], [password], [user_level], [verified], [verification_code], [enabled], [profile_picture], [phone], [status_deleted]) VALUES (N'2', N'admin', N'John5', N'Doe', N'john.doe@example.com', N'q1w2e3r4', N'1', N'1', null, N'1', null, N'02556776', null)
GO
GO
INSERT INTO [dbo].[users] ([id], [username], [first_name], [last_name], [email], [password], [user_level], [verified], [verification_code], [enabled], [profile_picture], [phone], [status_deleted]) VALUES (N'3', N'user2', N'Jane', N'Smith', N'jane.smith@example.com', N'password456', N'0', N'1', null, N'1', 0x6956424F5277304B47676F414141414E53556845556741414149414141414341434149414141424D585061634141414255556C45515652346E4F7A5451516E445141424530564957756770364B665257586655544E644556465247786830664966776F47506A503237665734736A482F65734B537078357764775841436F415641437341566743734146674273414A674263414B674255414B774257414B774157414777416D4146774171414651417241465941724142594162414359415841436F415641437341566743734146674273414A674263414B674255414B774257414B774157414777416D4146774171414651417241465941724142594162414359415841436F415641437341566743734146674273414A674263414B674255414B774257414B77413250692B7039367735504D373949516C5051417241465941724142594162414359415841436F415641437341566743734146674273414A674263414B674255414B774257414B774157414777416D4146774171414651417241465941724142594162414359415841436F415641437341566743734146674273414A674263414B674255414B774257414B774157414777416D4146774171414651417241465941724142594162414359415841436F415641437341566743734146674273414A674263444F414141412F2F39634E675158784562677A7741414141424A52553545726B4A6767673D3D, N'088599599595', null)
GO
GO
INSERT INTO [dbo].[users] ([id], [username], [first_name], [last_name], [email], [password], [user_level], [verified], [verification_code], [enabled], [profile_picture], [phone], [status_deleted]) VALUES (N'4', N'user3', N'Michael', N'Johnson', N'michael.johnson@example.com', N'password789', N'0', N'1', null, N'1', 0x6956424F5277304B47676F414141414E53556845556741414149414141414341434149414141424D585061634141414255556C45515652346E4F7A5451516E445141424530564957756770364B665257586655544E644556465247786830664966776F47506A503237665734736A482F65734B537078357764775841436F415641437341566743734146674273414A674263414B674255414B774257414B774157414777416D4146774171414651417241465941724142594162414359415841436F415641437341566743734146674273414A674263414B674255414B774257414B774157414777416D4146774171414651417241465941724142594162414359415841436F415641437341566743734146674273414A674263414B674255414B774257414B77413250692B7039367735504D373949516C5051417241465941724142594162414359415841436F415641437341566743734146674273414A674263414B674255414B774257414B774157414777416D4146774171414651417241465941724142594162414359415841436F415641437341566743734146674273414A674263414B674255414B774257414B774157414777416D4146774171414651417241465941724142594162414359415841436F415641437341566743734146674273414A674263444F414141412F2F39634E675158784562677A7741414141424A52553545726B4A6767673D3D, null, null)
GO
GO
INSERT INTO [dbo].[users] ([id], [username], [first_name], [last_name], [email], [password], [user_level], [verified], [verification_code], [enabled], [profile_picture], [phone], [status_deleted]) VALUES (N'5', N'john_doe', N'John', N'Doe', N'john@example.com', N'hashed_password_1', N'0', N'1', null, N'1', 0x6956424F5277304B47676F414141414E53556845556741414149414141414341434149414141424D585061634141414255556C45515652346E4F7A5451516E445141424530564957756770364B665257586655544E644556465247786830664966776F47506A503237665734736A482F65734B537078357764775841436F415641437341566743734146674273414A674263414B674255414B774257414B774157414777416D4146774171414651417241465941724142594162414359415841436F415641437341566743734146674273414A674263414B674255414B774257414B774157414777416D4146774171414651417241465941724142594162414359415841436F415641437341566743734146674273414A674263414B674255414B774257414B77413250692B7039367735504D373949516C5051417241465941724142594162414359415841436F415641437341566743734146674273414A674263414B674255414B774257414B774157414777416D4146774171414651417241465941724142594162414359415841436F415641437341566743734146674273414A674263414B674255414B774257414B774157414777416D4146774171414651417241465941724142594162414359415841436F415641437341566743734146674273414A674263444F414141412F2F39634E675158784562677A7741414141424A52553545726B4A6767673D3D, null, null)
GO
GO
INSERT INTO [dbo].[users] ([id], [username], [first_name], [last_name], [email], [password], [user_level], [verified], [verification_code], [enabled], [profile_picture], [phone], [status_deleted]) VALUES (N'6', N'jane_smith', N'Jane', N'Smith', N'jane@example.com', N'hashed_password_2', N'0', N'1', null, N'1', 0x6956424F5277304B47676F414141414E53556845556741414149414141414341434149414141424D585061634141414255556C45515652346E4F7A5451516E445141424530564957756770364B665257586655544E644556465247786830664966776F47506A503237665734736A482F65734B537078357764775841436F415641437341566743734146674273414A674263414B674255414B774257414B774157414777416D4146774171414651417241465941724142594162414359415841436F415641437341566743734146674273414A674263414B674255414B774257414B774157414777416D4146774171414651417241465941724142594162414359415841436F415641437341566743734146674273414A674263414B674255414B774257414B77413250692B7039367735504D373949516C5051417241465941724142594162414359415841436F415641437341566743734146674273414A674263414B674255414B774257414B774157414777416D4146774171414651417241465941724142594162414359415841436F415641437341566743734146674273414A674263414B674255414B774257414B774157414777416D4146774171414651417241465941724142594162414359415841436F415641437341566743734146674273414A674263444F414141412F2F39634E675158784562677A7741414141424A52553545726B4A6767673D3D, null, null)
GO
GO
INSERT INTO [dbo].[users] ([id], [username], [first_name], [last_name], [email], [password], [user_level], [verified], [verification_code], [enabled], [profile_picture], [phone], [status_deleted]) VALUES (N'7', N'testuytutyuytyu', N'dsftyuty', N'sdfdfyutyu', N'test@tetyutyus.bg', N'asdsd55567', N'0', N'0', N'BUDu6XJQJXMxGJKHZr66InehK7MEiE5NT4gcuoSkgyaXJVS8dnlL4TzQ37Bepqrh', N'0', null, null, null)
GO
GO
INSERT INTO [dbo].[users] ([id], [username], [first_name], [last_name], [email], [password], [user_level], [verified], [verification_code], [enabled], [profile_picture], [phone], [status_deleted]) VALUES (N'8', N'testuytutyuy54tyu', N'dsftyuty', N'sdfdfyutyu', N'test@tet54yutyus.bg', N'asdsd55567', N'0', N'0', N'fTpxLob57QqDkCplpsBbHO2kd1PvVH48wmswEm7nchXcxdNZ05Jb7wDkPLCCG0fF', N'0', null, null, null)
GO
GO
INSERT INTO [dbo].[users] ([id], [username], [first_name], [last_name], [email], [password], [user_level], [verified], [verification_code], [enabled], [profile_picture], [phone], [status_deleted]) VALUES (N'9', N'testuytuty45uy54tyu', N'dsftyuty', N'sdfdfyutyu', N'test345@tet54yutyus.bg', N'asdsd55567', N'0', N'0', N'iNtJuj4LlyNhMt5M8CAWHvYK45UBdxAyPiY2xG3CH4i3xmLC2Ru8L9XGA42TESCx', N'0', null, null, null)
GO
GO
INSERT INTO [dbo].[users] ([id], [username], [first_name], [last_name], [email], [password], [user_level], [verified], [verification_code], [enabled], [profile_picture], [phone], [status_deleted]) VALUES (N'10', N'testu1ytuty45uy54tyu', N'dsftyuty', N'sdfdfyutyu', N'test345@1tet54yutyus.bg', N'asdsd55567', N'0', N'0', N'mxDzAV8msjaBEhKkXn1VVaeaH5zuc6D3F4fRQq3FeH0GE6pz4a69Q6cJ3kmxHlwf', N'0', null, null, null)
GO
GO
INSERT INTO [dbo].[users] ([id], [username], [first_name], [last_name], [email], [password], [user_level], [verified], [verification_code], [enabled], [profile_picture], [phone], [status_deleted]) VALUES (N'11', N'dtestu1ytuty45uy54tyu', N'dsftyuty', N'sdfdfyutyu', N'tedst345@1tet54yutyus.bg', N'asdsd55567', N'0', N'0', N'3qf37roc5jJB3Psqq92H4QLB1lpv5VmphSkBZP8BnXx9j8x2Ru8IIEk1dETyOYpu', N'0', null, null, null)
GO
GO
INSERT INTO [dbo].[users] ([id], [username], [first_name], [last_name], [email], [password], [user_level], [verified], [verification_code], [enabled], [profile_picture], [phone], [status_deleted]) VALUES (N'12', N'dteau1ytuty45uy54tyu', N'dsftyuty', N'sdfdfyutyu', N'tedsat345@1tet54yutyus.bg', N'asdsd55567', N'0', N'0', N'YFAvj4R9ThJnZOTfEzuTWkOUdh83U0yvcJN5oEj5xladby1T6mKYyvrFH0xjVQ1i', N'0', null, null, null)
GO
GO
INSERT INTO [dbo].[users] ([id], [username], [first_name], [last_name], [email], [password], [user_level], [verified], [verification_code], [enabled], [profile_picture], [phone], [status_deleted]) VALUES (N'13', N'Cringe', N'dsftyuty', N'sdfdfyutyu', N't3eds2at345@1tet54yutyus.bg', N'asdsd55567', N'0', N'0', N'NmfI2EFUAms0tQEnW33tylDTnwVfY5yAEcDnB31pbEQw00rxzdEMQP9d9aXCKh8p', N'0', 0x89504E470D0A1A0A0000000D49484452000000800000008008020000004C5CF69C0000015149444154789CECD34109C3400044D15216BA0A7A29F4565DF51335D1151511B18747C87F0A063E33F6EDF5B8B231FF7AC292A71E707705C00A8015002B005600AC005801B0026005C00A8015002B005600AC005801B0026005C00A8015002B005600AC005801B0026005C00A8015002B005600AC005801B0026005C00A8015002B005600AC005801B0026005C00A8015002B005600AC005801B0026005C00A8015002B005600AC005801B0026005C00A8015002B005600AC00D8F8BEA7DEB0E4F33BF484253D002B005600AC005801B0026005C00A8015002B005600AC005801B0026005C00A8015002B005600AC005801B0026005C00A8015002B005600AC005801B0026005C00A8015002B005600AC005801B0026005C00A8015002B005600AC005801B0026005C00A8015002B005600AC005801B0026005C00A8015002B005600AC005801B0026005C0CE000000FFFF5C360417C446E0CF0000000049454E44AE426082, null, null)
GO
GO
INSERT INTO [dbo].[users] ([id], [username], [first_name], [last_name], [email], [password], [user_level], [verified], [verification_code], [enabled], [profile_picture], [phone], [status_deleted]) VALUES (N'14', N'sdfsdfsd', N'sdfsdf', N'sdfsdf', N'sdfsdf@fdsfgs.co', N'3e3r4', N'0', N'0', N'e7mKRu0dqdDfc1ashIDMrLNnolAHbLXM4wVyDfHFJj5ZHOT2yDCdzBBgd0vLGyE2', N'0', null, null, null)
GO
GO
INSERT INTO [dbo].[users] ([id], [username], [first_name], [last_name], [email], [password], [user_level], [verified], [verification_code], [enabled], [profile_picture], [phone], [status_deleted]) VALUES (N'15', N'sdfsdfsd3', N'sdfsdf', N'sdfsdf', N'sdf3sdf@fdsfgs.co', N'3e3r4', N'0', N'0', N'MBRbhE31RhIT6aABruXcooaU5suUH70VCQuKBrWmac43Gs5riX7UphLjwxutjE6D', N'0', null, null, null)
GO
GO
INSERT INTO [dbo].[users] ([id], [username], [first_name], [last_name], [email], [password], [user_level], [verified], [verification_code], [enabled], [profile_picture], [phone], [status_deleted]) VALUES (N'17', N'sdfsdfsd335', N'sdfsdf', N'sdfsdf', N'gegeg@gege.geo', N'3e3r4', N'0', N'0', N'X54rx8qkLY76Dp2TS5f4B0DZgjafIGtkE4InvgkE2gd1hHmsIXaSqYTxkdCSly5z', N'0', null, null, null)
GO
GO
INSERT INTO [dbo].[users] ([id], [username], [first_name], [last_name], [email], [password], [user_level], [verified], [verification_code], [enabled], [profile_picture], [phone], [status_deleted]) VALUES (N'18', N'ivailo', N'ivailo', N'tsatsov', N'maimunka@abv.bg', N'shkembechorba', N'0', N'0', N'LV4ErUCdjrzQwhH0hlDGqqjFXhNUV61SuvoM01lOvOS4tJhJWZ0dGZn4JsSPVCM6', N'0', null, null, null)
GO
GO
SET IDENTITY_INSERT [dbo].[users] OFF
GO

-- ----------------------------
-- Table structure for wallets
-- ----------------------------
DROP TABLE [dbo].[wallets]
GO
CREATE TABLE [dbo].[wallets] (
[id] int NOT NULL IDENTITY(1,1) ,
[owner] int NOT NULL ,
[balance] decimal(18) NULL ,
[status_deleted] int NOT NULL DEFAULT ((0)) ,
[overdraft_enabled] int NOT NULL DEFAULT ((0)) ,
[interest_rate] float(53) NULL ,
[number_of_wallet] int NULL 
)


GO
DBCC CHECKIDENT(N'[dbo].[wallets]', RESEED, 1006)
GO

-- ----------------------------
-- Records of wallets
-- ----------------------------
SET IDENTITY_INSERT [dbo].[wallets] ON
GO
INSERT INTO [dbo].[wallets] ([id], [owner], [balance], [status_deleted], [overdraft_enabled], [interest_rate], [number_of_wallet]) VALUES (N'1', N'3', N'10835', N'0', N'0', N'0.02', N'1')
GO
GO
INSERT INTO [dbo].[wallets] ([id], [owner], [balance], [status_deleted], [overdraft_enabled], [interest_rate], [number_of_wallet]) VALUES (N'2', N'2', N'2010', N'0', N'0', N'0.02', N'1')
GO
GO
INSERT INTO [dbo].[wallets] ([id], [owner], [balance], [status_deleted], [overdraft_enabled], [interest_rate], [number_of_wallet]) VALUES (N'3', N'3', N'40200', N'0', N'0', N'0.02', N'2')
GO
GO
INSERT INTO [dbo].[wallets] ([id], [owner], [balance], [status_deleted], [overdraft_enabled], [interest_rate], [number_of_wallet]) VALUES (N'4', N'5', N'1170', N'0', N'0', N'0.02', N'1')
GO
GO
INSERT INTO [dbo].[wallets] ([id], [owner], [balance], [status_deleted], [overdraft_enabled], [interest_rate], [number_of_wallet]) VALUES (N'5', N'4', N'4250', N'0', N'0', N'0.02', N'1')
GO
GO
INSERT INTO [dbo].[wallets] ([id], [owner], [balance], [status_deleted], [overdraft_enabled], [interest_rate], [number_of_wallet]) VALUES (N'6', N'3', N'1000', N'0', N'0', N'0.02', N'3')
GO
GO
INSERT INTO [dbo].[wallets] ([id], [owner], [balance], [status_deleted], [overdraft_enabled], [interest_rate], [number_of_wallet]) VALUES (N'7', N'2', N'0', N'0', N'0', N'0.02', N'2')
GO
GO
SET IDENTITY_INSERT [dbo].[wallets] OFF
GO

-- ----------------------------
-- Indexes structure for table cards
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table cards
-- ----------------------------
ALTER TABLE [dbo].[cards] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table interest_rates
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table interest_rates
-- ----------------------------
ALTER TABLE [dbo].[interest_rates] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table savings_wallets
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table savings_wallets
-- ----------------------------
ALTER TABLE [dbo].[savings_wallets] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table transactions
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table transactions
-- ----------------------------
ALTER TABLE [dbo].[transactions] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table users
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table users
-- ----------------------------
ALTER TABLE [dbo].[users] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Uniques structure for table users
-- ----------------------------
ALTER TABLE [dbo].[users] ADD UNIQUE ([username] ASC)
GO

-- ----------------------------
-- Indexes structure for table wallets
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table wallets
-- ----------------------------
ALTER TABLE [dbo].[wallets] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[cards]
-- ----------------------------
ALTER TABLE [dbo].[cards] ADD FOREIGN KEY ([card_holder]) REFERENCES [dbo].[users] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[savings_wallets]
-- ----------------------------
ALTER TABLE [dbo].[savings_wallets] ADD FOREIGN KEY ([owner]) REFERENCES [dbo].[users] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
ALTER TABLE [dbo].[savings_wallets] ADD FOREIGN KEY ([from_wallet]) REFERENCES [dbo].[wallets] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[transactions]
-- ----------------------------
ALTER TABLE [dbo].[transactions] ADD FOREIGN KEY ([wallet_id]) REFERENCES [dbo].[wallets] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

-- ----------------------------
-- Foreign Key structure for table [dbo].[wallets]
-- ----------------------------
ALTER TABLE [dbo].[wallets] ADD FOREIGN KEY ([owner]) REFERENCES [dbo].[users] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
