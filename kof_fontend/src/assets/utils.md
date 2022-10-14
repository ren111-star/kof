const isSameSet = (s1, s2) => {
    const isSame = (a, b) => {
        const values = [...a];
        for (let val of values) {
            if (!b.has(val)) return false;
        }
        return true;
    }

    return isSame(s1, s2) && isSame(s2, s1)
}
