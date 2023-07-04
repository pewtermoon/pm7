// Very simple example of using patterns + non-real-time synthesis
// Note: this can be executed on the CMD line with sclang
// (no need to s.boot or anything like that!)
// sclang nrt_patterns1.sc
// play nrt_patterns1.wav
var p1, p2, pp, odir, p3, p4, dur1, dur2, s1, s2, ext, reps, scr, pp1, pp2
, p5, p6, p7, p8, pp3, pp4, s3, p9, s4, p10, pp5, s5, s6, s7, s8,
p11, p12, p13, p14, p15, p16, pp6, pp7, pp8, ffrq, s9;

// Set output path
odir = thisProcess.nowExecutingPath.dirname;

ffrq = 440;

SynthDef(\syn1, { |freq = 440, amp = 0.9, scal = 0.1|
    var sig, env;
	sig = SinOsc.ar([freq, freq], 0, scal);
    env = Env.perc(0.0001, 
    //0.0625,
    0.5, 
    1, -8);
//	env = Env([0,0.9,0.9,0], [0.07, 0.01, 0.1]*scal);
	env = EnvGen.kr(env, doneAction: 2);
	sig = Out.ar(0, sig*env);
}).writeDefFile;

// pattern
ext = 8; // Length of smallest repeating unit
reps = 2; // # times to repeat this unit

// s9 = Array.series(24, 1, 1);
//s9 = Array.geom(24, 1, 3);
//s9 = Array.fib(8, 1, 32); // start from 32 with step 2.

// s9 = Array.series(4, 1, 1);
// s8 = Array.series(4, 1, 1);

s9 = Array.interpolation(32, 0.4, 1.6);
s8 = Array.interpolation(32, 1.6, 0.4);

scr = s8 ++ s9;

// scr.postln;

// 1st
p1 = Pbind(
    \instrument, \syn1,
    \freq, Pseq(scr*ffrq, ext),
	\dur, (1/scr.size)*ext);

// // Combining things in parallel
// pp1 = Ppar([p1, p2]);
// pp2 = Ppar([p2, p10]);
// pp3 = Ppar([p3, p9]);
// pp4 = Ppar([p4, p12]);
// pp5 = Ppar([p5, p15]);
// pp6 = Ppar([p6, p14]);
// pp7 = Ppar([p7, p13]);
// pp8 = Ppar([p8, p16]);

// Combining things in series
// scr = [pp1, pp2, pp3, pp4, pp5, pp6, pp7, pp8];

// scr = [pp2, pp5, pp1, pp6, pp3, pp7, pp4, pp8];
scr = [p1];

pp = Pseq(scr, reps);

// render the pattern to wav
pp.render(
    path: odir +/+ "nrt_patterns1.wav"
	, maxTime: ext*reps*scr.size
	, headerFormat: "WAV"
);

//exit sclang (note: if include this, doesn't write wav file!)
// need to manually Ctrl+C when reaches end to exit...
// 0.exit;