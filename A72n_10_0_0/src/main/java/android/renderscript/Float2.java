package android.renderscript;

public class Float2 {
    public float x;
    public float y;

    public Float2() {
    }

    public Float2(Float2 data) {
        this.x = data.x;
        this.y = data.y;
    }

    public Float2(float x2, float y2) {
        this.x = x2;
        this.y = y2;
    }

    public static Float2 add(Float2 a, Float2 b) {
        Float2 res = new Float2();
        res.x = a.x + b.x;
        res.y = a.y + b.y;
        return res;
    }

    public void add(Float2 value) {
        this.x += value.x;
        this.y += value.y;
    }

    public void add(float value) {
        this.x += value;
        this.y += value;
    }

    public static Float2 add(Float2 a, float b) {
        Float2 res = new Float2();
        res.x = a.x + b;
        res.y = a.y + b;
        return res;
    }

    public void sub(Float2 value) {
        this.x -= value.x;
        this.y -= value.y;
    }

    public static Float2 sub(Float2 a, Float2 b) {
        Float2 res = new Float2();
        res.x = a.x - b.x;
        res.y = a.y - b.y;
        return res;
    }

    public void sub(float value) {
        this.x -= value;
        this.y -= value;
    }

    public static Float2 sub(Float2 a, float b) {
        Float2 res = new Float2();
        res.x = a.x - b;
        res.y = a.y - b;
        return res;
    }

    public void mul(Float2 value) {
        this.x *= value.x;
        this.y *= value.y;
    }

    public static Float2 mul(Float2 a, Float2 b) {
        Float2 res = new Float2();
        res.x = a.x * b.x;
        res.y = a.y * b.y;
        return res;
    }

    public void mul(float value) {
        this.x *= value;
        this.y *= value;
    }

    public static Float2 mul(Float2 a, float b) {
        Float2 res = new Float2();
        res.x = a.x * b;
        res.y = a.y * b;
        return res;
    }

    public void div(Float2 value) {
        this.x /= value.x;
        this.y /= value.y;
    }

    public static Float2 div(Float2 a, Float2 b) {
        Float2 res = new Float2();
        res.x = a.x / b.x;
        res.y = a.y / b.y;
        return res;
    }

    public void div(float value) {
        this.x /= value;
        this.y /= value;
    }

    public static Float2 div(Float2 a, float b) {
        Float2 res = new Float2();
        res.x = a.x / b;
        res.y = a.y / b;
        return res;
    }

    public float dotProduct(Float2 a) {
        return (this.x * a.x) + (this.y * a.y);
    }

    public static float dotProduct(Float2 a, Float2 b) {
        return (b.x * a.x) + (b.y * a.y);
    }

    public void addMultiple(Float2 a, float factor) {
        this.x += a.x * factor;
        this.y += a.y * factor;
    }

    public void set(Float2 a) {
        this.x = a.x;
        this.y = a.y;
    }

    public void negate() {
        this.x = -this.x;
        this.y = -this.y;
    }

    public int length() {
        return 2;
    }

    public float elementSum() {
        return this.x + this.y;
    }

    public float get(int i) {
        if (i == 0) {
            return this.x;
        }
        if (i == 1) {
            return this.y;
        }
        throw new IndexOutOfBoundsException("Index: i");
    }

    public void setAt(int i, float value) {
        if (i == 0) {
            this.x = value;
        } else if (i == 1) {
            this.y = value;
        } else {
            throw new IndexOutOfBoundsException("Index: i");
        }
    }

    public void addAt(int i, float value) {
        if (i == 0) {
            this.x += value;
        } else if (i == 1) {
            this.y += value;
        } else {
            throw new IndexOutOfBoundsException("Index: i");
        }
    }

    public void setValues(float x2, float y2) {
        this.x = x2;
        this.y = y2;
    }

    public void copyTo(float[] data, int offset) {
        data[offset] = this.x;
        data[offset + 1] = this.y;
    }
}
