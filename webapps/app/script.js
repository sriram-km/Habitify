const colors = [
    "#E45A84",
    "#FFD478",
    "#BA53DE",
    "#393E46",
    "#497285",
    "#3AB1C8",
    "#8DC6FF",
    "#B2E672",
    "#B17179"
];

const vm = new Vue({
    el: "#app",
    data: {
        habits: [{
                title: 'GYM',
                reps: 30,
                initial: 30,
                complete: 1,
                random: "#E45A84",
                finished: false
            },
            {
                title: 'Yoga',
                reps: 30,
                initial: 30,
                complete: 1,
                random: "#E45A84",
                finished: false
            },
            {
                title: 'Read Books',
                reps: 30,
                initial: 30,
                complete: 1,
                random: "#E45A84",
                finished: false
            },
            {
                title: "Don't cuss",
                reps: 30,
                initial: 30,
                complete: 1,
                random: "#E45A84",
                finished: false
            }
        ],
        newHabit: '',
        reps: '',
        initial: 0,
        complete: 0,
        colors: colors,
        finished: false
    },
    methods: {
        addHabit() {
            if (this.newHabit && this.reps) {
                this.habits.push({
                    title: this.newHabit,
                    reps: this.reps,
                    initial: this.reps,
                    complete: 0,
                    random: this.colors[Math.floor(Math.random() * this.colors.length)],
                    finished: false
                });
            }
            this.newHabit = '';
            this.reps = '';
        },
        removeHabit(habit) {
            this.habits.$remove(habit);
            console.log(this.habits);
        },
        completeReps(habit) {
            if (habit.reps > 0) {
                habit.reps -= 1;
                habit.complete += 1;
            }
            if (habit.reps === 0) {
                habit.finished = true;
            }
        }
    }
});