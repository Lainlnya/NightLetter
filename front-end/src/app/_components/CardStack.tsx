import { useSpring, animated } from "react-spring";

const to = (i: number) => ({
  x: 0,
  y: i * -4,
  scale: 1,
  rot: -10 + Math.random() * 20,
  delay: i * 100,
});
const from = (_i: number) => ({ x: 0, rot: 0, scale: 1.5, y: -1000 });

export default function CardStack({ handleClick }) {
  const [props, api] = useSpring(4, (i: number) => ({
    ...to(i),
    from: from(i),
  }));
}
