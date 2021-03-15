/*
TERM VIEW
*/
SELECT term.termTitle FROM term;

/*
TERM ID
*/
SELECT term._id FROM term WHERE term.termTitle = 'Term1';

/*
TERM DETAIL
*/
SELECT 
	term.termTitle,
	term.termStart,
	term.termEnd
From 
	term
WHERE
	term._id = 1;
	
SELECT
	course.courseTitle,
	coursedetails._id,
	coursedetails.courseId,
	coursedetails.termId,
	coursedetails.notes,
	coursedetails.startDate,
	coursedetails.startAlarmEnabled,
	coursedetails.endDate,
	coursedetails.endAlarmEnabled,
	coursedetails.`status`
FROM
	course
JOIN
	coursedetails
ON
	course._id = coursedetails.courseId AND coursedetails.termId = 1;

/*
TERM_COURSE_DETAILS_VIEW
*/
SELECT 
	course.courseTitle,
	coursedetails.startDate,
	coursedetails.endDate,
	coursedetails.startAlarmEnabled,
	coursedetails.endAlarmEnabled,
	coursedetails.notes
FROM
	course
JOIN coursedetails ON
	course._id = coursedetails.courseId
WHERE
	coursedetails._id = 1;

/*
Course Details View
*/
SELECT 
	course.courseTitle
FROM
	course
WHERE 
	course._id = 1;
	
SELECT
	mentor.mentorName
FROM
	mentor
JOIN
	mentorassignments ON mentor._id = mentorassignments.mentorId AND mentorassignments.courseId = 1;
	
/*
MENTOR LIST
*/
select mentorName FROM mentor;

/*
MENTOR DETAIL (selected by id)
*/

SELECT * FROM mentor WHERE mentor._id = 1;

SELECT
	course.courseTitle
FROM
	course
JOIN
	mentorassignments ON course._id = mentorassignments.courseId AND mentorassignments.mentorId = 1;