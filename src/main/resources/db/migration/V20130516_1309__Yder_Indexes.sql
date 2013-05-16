ALTER TABLE `Yderregister`
ADD INDEX `krsidx` (`ModifiedDate` ASC, `PID` ASC)
, ADD INDEX `validix` (`ValidTo` ASC, `ValidFrom` ASC) ;

ALTER TABLE `Yderregister`
ADD INDEX `ididx` (`Id` ASC) ;


ALTER TABLE `YderregisterPerson`
ADD INDEX `krsidx` (`ModifiedDate` ASC, `PID` ASC)
, ADD INDEX `valididx` (`ValidTo` ASC, `ValidFrom` ASC) ;

ALTER TABLE `YderregisterPerson`
ADD INDEX `ididx` (`Id` ASC) ;
