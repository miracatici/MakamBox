/** This file is part of MakamBox.

    MakamBox is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    MakamBox is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with MakamBox.  If not, see <http://www.gnu.org/licenses/>.
 */ 

Welcome 

This software is a tool that can help user to input culture-specific settings for  MakamBox, culture-specific music analysis software.

This process contains 3 parts. First part is Note Settings. In this part, user will specify the name of notes and frequency ratios with respect to the first note. This information serves as a theoretical reference which helps initialization of intonation analysis. For example, frequency ratio of A5 with reference to A4 is 2. 

For the 12-yet tuning system, the note list can be specified as:

C - 1.00   	
C# - 1.059 
D - 1.122  (ratio wrt first note)
D# - 1.189 (ratio wrt first note)
.
.
.
C - 2.00   (Octave)

Second part is the Ahenk Settings. Ahenk refers to key transposition (Ex: Bb key used for some woodwind instruments). User will specify the ahenk name, reference note and frequency of the reference note for each ahenk. This data will be used for key transposition (when pitch shifting needs to be applied).

Third part is the Makam Settings. For each makam, a template histogram file is needed, which is computed/learned from recordings using another tool. The user needs to specify the text file path containing the histogram. Also, each makam has a tonic note, "karar". This note may be called as "ending note" or "starting note". With this note, MakamBox compute shifting amount to transpose to user-specified ahenk.

At the end of the process, user needs to save all settings with given name to a Java SER file. It'll be used to set culture specific features of the MakamBox. The name of this file must have contain only letters and no spaces. The DataTool saves the file to folder where DataTool is located.