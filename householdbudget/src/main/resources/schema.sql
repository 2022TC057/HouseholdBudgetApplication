CREATE TABLE IF NOT EXISTS incomelist (
id VARCHAR(8) PRIMARY KEY,
income VARCHAR(256),
date VARCHAR(10),
tag VARCHAR(256),
memo VARCHAR(256)
);
CREATE TABLE IF NOT EXISTS expenditurelist (
id VARCHAR(8) PRIMARY KEY,
expenditure VARCHAR(256),
date VARCHAR(10),
tag VARCHAR(256),
memo VARCHAR(256)
);
