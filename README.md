
# Data Stats

## Endpoints

### POST /event

This route receives 3 values separated by a comma (,) where:

`timestamp`: An integer with the Unix timestamp in millisecond resolution when the event happened.

`𝑥`: A real number with a fractional part of up to 10 digits, always in 0..1.

`𝑦`: An integer in 1,073,741,823..2,147,483,647.

### GET /stats

Returns statistics about the data points that lie within the past 60 seconds separated by comma:

#### Response 

`Total, Sum 𝑥, Avg 𝑥, Sum 𝑦, Avg 𝑦`

## Project Architecture

### Event Controller 
Contains the logic of the `/event` request. 

Has a  **PayloadService**  that process the request Payload y transforms it into a List of `DataEvent` objects.

And also an **EventService**, that took cares of storing the relevant data into the data structures. 

### Stats Controller 
Contains the logic of the `/stats` request. 

Has an **EventService**  that calculate the stats for the moment of the request using the stored data.

## Data Structures

`lastUpdate` Timestamp variable, that stores the timestamp of the last POST request.

`int[] countX  ` Array of int of **size 60**, that will store in the position `i` the number of **x** data that belongs to the second `i`, with `i` between now and 60 seconds in the past.

`int[] countY  ` Same as `countX` but for **y** data.

`double[] sumX  ` Array of double of **size 60**, where in the the position `i`  will be stored the sum of values of `y` that belongs to second `i`. 

`long[] sumY` Array of long of **size 60**, where in the the position `i`  will be stored the sum of values of `y` that belongs to second `i`. 

Big O notation is used to describe how running time or space grows as the input grows. Because all the arrays are of fixed size (60), the number of operations (get the element on position `i` or write on position `i`) performed are constraint and doesn't grow as the input grows. Then the algorithm described in the following section is `O(1)` both in time and space.

## Algorithms 

### Save data

- Get difference (`diffPrevUpdate`) of time in seconds between the `lastUpdate` and now. If `lastUpdate` is null, then this is the last time data will be saved, so we'll set it as now.

- First we will prepare the data structures for loading the data.

	- If `diffPrevUpdate` is greater than 60, then the data previously stored is too old and we won't need it anymore, so we can set all the arrays back to 0.

	- If `diffPrevUpdate` is between 0 and 60, then there is some data stored that we need to keep and some of it is already outdated. 

		- We first need to find out which is the `lastValid` position of the array, and that is de difference between 60 seconds and `diffPrevUpdate`. 

		- From index 0 to `lastValid` in the arrays is stored the "oldest" data in time (considering now), so we need to shift the array to the right. In the positions on the left we will fill the arrays with 0, as this positions (this timestamps) didn't exist in previous payloads.

- After this, we can take care of the new data from the payload, just by calculating how many seconds apart is each Data Event timestamp from now.

- For every Data Event that is in a range of 60 seconds, we will save the data of `x` and `y` in  `sumX` and `sumY` and increment the count in `countX` and `countY` for the corresponding position.

- Lastly, we'll update the `lastUpdate` timestamp.

### Get stats
`Total` :   Sum of `countX`, as this is the total number of Data Events stored. Returns an int.

`Sum x` : Sum of values in `sumX`. Returns a double.

`Sum y` : Sum of values in `sumY`. Returns a long.

`Avg x` : `Sum x` / `Total`. Returns a double. *Important*: For more than 1000000 Data Events (`Total` > 1000000) this could overflow. 

`Avg y` : `Sum y` / `Total`. Returns a double. 

