package com.appian.manchesterunitednews.app.widget;

public class SectionWrapper<S extends Section<C>, C> {
    private boolean isSection;
    private S section;
    private C child;
    private int sectionPosition;
    private int childPosition;

    SectionWrapper(S section, int sectionPosition) {
        this.section = section;
        this.sectionPosition = sectionPosition;
        this.isSection = true;
        this.childPosition = -1;
    }

    SectionWrapper(C child, int sectionPosition, int childPosition) {
        this.child = child;
        this.sectionPosition = sectionPosition;
        this.isSection = false;
        this.childPosition = childPosition;
    }

    boolean isSection() {
        return isSection;
    }

    public S getSection() {
        return section;
    }

    public C getChild() {
        return child;
    }

    int getSectionPosition() {
        return sectionPosition;
    }

    int getChildPosition() {
        return childPosition;
    }
}
