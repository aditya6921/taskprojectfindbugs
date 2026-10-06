# NOTES

## Summary of Changes

Fixed several bugs in the Task Tracker application.

- Fixed invalid status handling so invalid values return a 400 Bad Request instead of a server error.
- Improved pagination by moving pagination from Java memory to the database using `Page` and `Pageable`.
- Fixed SQL search wildcard handling by escaping `%`, `_`, and `\` characters.
- Fixed search behavior so changing the search or status filter resets pagination to page 1.
- Fixed loading/error state handling in the frontend.
- Removed the artificial backend delay that caused slow responses.

Detailed bug descriptions, testing steps, root causes, and fixes are documented in the handwritten notes.

## What I Chose Not to Change

- I did not add a priority filter because the current requirements do not explicitly require one.
- I did not change the possible stale-response race condition because it could not be reproduced reliably during testing.
