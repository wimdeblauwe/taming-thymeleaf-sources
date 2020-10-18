function userPopupMenu() {
    return {
        show: false,
        toggleVisibility() {
            this.show = !this.show;
        },
        close() {
            this.show = false;
        },
        isVisible() {
            return this.show === true;
        }
    };
}
