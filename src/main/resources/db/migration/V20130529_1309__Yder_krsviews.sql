
-- -----------------------------------------------------
-- Someone has to create the SKRS tables first time
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SKRSViewMapping` (
  `idSKRSViewMapping` BIGINT(15) NOT NULL AUTO_INCREMENT ,
  `register` VARCHAR(255) NOT NULL ,
  `datatype` VARCHAR(255) NOT NULL ,
  `version` INT NOT NULL ,
  `tableName` VARCHAR(255) NOT NULL ,
  `createdDate` TIMESTAMP NOT NULL ,
  PRIMARY KEY (`idSKRSViewMapping`) ,
  INDEX `idx` (`register` ASC, `datatype` ASC, `version` ASC) ,
  UNIQUE INDEX `unique` (`register` ASC, `datatype` ASC, `version` ASC) )
  ENGINE = InnoDB;
CREATE  TABLE IF NOT EXISTS `SKRSColumns` (
  `idSKRSColumns` BIGINT(15) NOT NULL AUTO_INCREMENT ,
  `viewMap` BIGINT(15) NOT NULL ,
  `isPID` TINYINT NOT NULL ,
  `tableColumnName` VARCHAR(255) NOT NULL ,
  `feedColumnName` VARCHAR(255) NULL ,
  `feedPosition` INT NOT NULL ,
  `dataType` INT NOT NULL ,
  `maxLength` INT NULL ,
  PRIMARY KEY (`idSKRSColumns`) ,
  INDEX `viewMap_idx` (`viewMap` ASC) ,
  UNIQUE INDEX `viewColumn` (`tableColumnName` ASC, `viewMap` ASC) ,
  CONSTRAINT `viewMap`
  FOREIGN KEY (`viewMap` )
  REFERENCES `SKRSViewMapping` (`idSKRSViewMapping` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

-- ---------------------------------------------------------------------------------------------------------------------
-- Yderregister

INSERT IGNORE INTO SKRSViewMapping (register, datatype, version, tableName, createdDate) VALUES ('yderregister', 'yder', 1, 'Yderregister', NOW());
INSERT IGNORE INTO SKRSColumns (viewMap, isPID, tableColumnName, feedColumnName, feedPosition, dataType, maxLength) VALUES
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 1, 'PID',        NULL,             0,  -5, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'Id',         'Id',             1,  12, 32),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'HistIdYder', 'HistIdYder',     2,  12, 16),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'AmtKodeYder', 'AmtKodeYder',   3,  12, 2),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'AmtTxtYder', 'AmtTxtYder',     4,  12, 60),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'YdernrYder', 'YdernrYder',     5,  12, 6),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'PrakBetegn', 'PrakBetegn',     6,  12, 50),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'AdrYder', 'AdrYder',           7,  12, 50),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'PostnrYder', 'PostnrYder',     8,  12, 4),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'PostdistYder', 'PostdistYder', 9,  12, 20),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'TilgDatoYder', 'TilgDatoYder', 10, 12, 8),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'AfgDatoYder', 'AfgDatoYder',   11, 12, 8),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'HvdSpecKode', 'HvdSpecKode',   12, 12, 2),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'HvdSpecTxt', 'HvdSpecTxt',     13, 12, 60),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'HvdTlf', 'HvdTlf',             14, 12, 8),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'EmailYder', 'EmailYder',       15, 12, 50),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'WWW', 'WWW',                   16, 12, 78),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'ModifiedDate', 'ModifiedDate', 17, 93, 12),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'ValidFrom', 'ValidFrom',       18, 93, 12),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='yder' AND version=1), 0, 'ValidTo', 'ValidTo',           19, 93, 12);

INSERT IGNORE INTO SKRSViewMapping (register, datatype, version, tableName, createdDate) VALUES ('yderregister', 'person', 1, 'YderregisterPerson', NOW());
INSERT IGNORE INTO SKRSColumns (viewMap, isPID, tableColumnName, feedColumnName, feedPosition, dataType, maxLength) VALUES
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='person' AND version=1), 1, 'PID',           NULL,                0,  -5, NULL),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='person' AND version=1), 0, 'Id',           'Id',                 1,  12, 32),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='person' AND version=1), 0, 'HistIdPerson', 'HistIdPerson',       2,  12, 16),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='person' AND version=1), 0, 'YdernrPerson', 'YdernrPerson',       3,  12, 6),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='person' AND version=1), 0, 'TilgDatoPerson', 'TilgDatoPerson',   4,  12, 8),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='person' AND version=1), 0, 'AfgDatoPerson', 'AfgDatoPerson',     5,  12, 8),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='person' AND version=1), 0, 'CprNr', 'CprNr',                     6,  12, 10),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='person' AND version=1), 0, 'PersonrolleKode', 'PersonrolleKode', 7,  12, 2),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='person' AND version=1), 0, 'PersonrolleTxt', 'PersonrolleTxt',   8,  12, 60),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='person' AND version=1), 0, 'ModifiedDate', 'ModifiedDate',       9,  93, 12),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='person' AND version=1), 0, 'ValidFrom', 'ValidFrom',             10, 93, 12),
((SELECT idSKRSViewMapping FROM SKRSViewMapping WHERE register='yderregister' AND datatype='person' AND version=1), 0, 'ValidTo', 'ValidTo',                 11, 93, 12);
