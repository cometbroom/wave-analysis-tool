<div style="display: flex; align-items: center; margin: 0.5rem 1rem; padding: 0rem 0rem; border-bottom: 1px solid rgba(136,136,136,0.68)">
<div style="display: grid">
  <img style="grid-column: 1; grid-row: 1; padding: 5px 5px" width="100" src="./github-assets/wavetoolicon.png">
</div>
<div style="width: 2rem"></div>
<h1 style="padding-left: 10px; border-bottom: none">Wave Analysis Tool</h1>
</div>

### Visualization tool for analyzing waveforms and how they can affect each other in unpredictable, undetermined ways. In future versions you can export your waveforms to a wav file or a vector graphics extension for further analysis.

### Table of Contents

- [Run](#run)
- [Build](#build)
- [To-do](#to-do)

## Run
If you have java 21 installed, it's very easy. Just download the executable jar file in the release section and run. 
It's on my to-do list to build platform dependent executables. Lower priority to be honest as the project currently needs many more features.

## Build
`mvn clean package`
You'll have an executable jar under [projectDir/target](./target) (You'll have to clone/fork build the project first)

## To-do
- Implement the button for export. Start with vector graphics and only the resulting waveform
- Add a control for changing waveform type (Currently only Sinusoidal)
- Add hover on charts to show details about points as you hover over them
  - Show modulation effect on the popup toolbar for analysis
- Add option for choosing internal sample rate. View resolution is much lower (500hz for view, 88200 for calculation) will have to remain the same in light of performance
- Add wav file export functionality
- Add a 256 frame navigator for simple waveforms where FFT is possible (Not chaos)
- Add formula export functionality with an estimate on periodicity. Needs multiple stages of FFT
- Optimize for extreme sample rates needed for chaos waveforms
- Implement algorithm to eliminate CPU Cycle effect on initial waveform conditions (Chaos Synthesis)
