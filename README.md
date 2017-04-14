# BusinessStats

An Android app created as part of my CS302 Software Engineering Course Project.

Link to docs- https://github.com/NIIT-Software-Engineering/Akash-Ashwin-Pilgaonkar-Jai-Ayush-Amit

## Features
It is a business management application for a mock tours and travels company which-
* Reads statistical data of the business from a remote SQL database
* Displays it in the form of a graph or a table
* Sends an auto generated Email every year to the travel agent having done the most number of bookings
* Import Multiple *.xls files containing flight schedules of different airlines and standardize them into a single file (Partially implemented)

## Usage
* Click on the **Process** button to get data from the remote database and store it in a local SQLite database
* Select the **year range** and **category** to sort records by
* Choose either the **Table** or **Graph** option to view the records
* Press the **Display** button to view the results

## SDK Version
`minSdkVersion 21
targetSdkVersion 23`

## Libraries Used
[MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)

[SortableTableView](https://github.com/ISchwarz23/SortableTableView)

## Screenshots
![Main Screen](https://cloud.githubusercontent.com/assets/13731530/25041974/e990ce08-2131-11e7-9376-4b989b242a19.jpg)
![Settings Screen](https://cloud.githubusercontent.com/assets/13731530/25041973/e9885a5c-2131-11e7-9ebb-3239ba2cca0b.jpg)
![Graph View](https://cloud.githubusercontent.com/assets/13731530/25042075/c7c5e7ee-2132-11e7-9a39-f65e143f3a76.jpg)

![Table View](https://cloud.githubusercontent.com/assets/13731530/25041972/e9841014-2131-11e7-976c-dce0fe655ec9.jpg)

## TODO
* Implement in-app help section
* Implement notification display even when the app is closed
* Finish implementation of flight schedules section
* Remove unused code
