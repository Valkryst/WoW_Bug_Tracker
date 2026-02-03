USE `characters`;

DELIMITER //

DROP PROCEDURE IF EXISTS getAllRawBugReports;

CREATE PROCEDURE getAllRawBugReports()
BEGIN
	SELECT * FROM `bugreport`;
END//



DROP PROCEDURE IF EXISTS setButReportStatus;

CREATE PROCEDURE setButReportStatus(`varBugReportId` INT, `varBugReportStatus` INT)
BEGIN
	UPDATE `bugreport` SET `status` = `varBugReportStatus` WHERE `id` = `varBugReportId`;
END//



DROP PROCEDURE IF EXISTS deleteBugReport;

CREATE PROCEDURE deleteBugReport(`varBugReportId` INT)
BEGIN
	DELETE FROM `bugreport` WHERE id = `varBugReportId`;
END//

DELIMITER ;