import { Server } from "azle";
import express, { Request } from "express";
import cors from "cors";

let test = {
  hello: "",
};

let scan = {
  code: "7777-7777-7777",
  // status: "activated",
};

let moment = {
  text: "I’ll be by your side",
  title: "BOBBY",
};

let myMoments = [
  {
    text: "I'll be by your side",
    title: "BOBBY",
    artist: "iKON",
  },
  {
    text: "We go up, up, up, up",
    title: "Boy With Luv",
    artist: "BTS",
  },
  {
    text: "I'm the biggest hit on this stage",
    title: "Killing Voice",
    artist: "Stray Kids",
  },
  {
    text: "We are one, all for one",
    title: "Wannabe",
    artist: "ITZY",
  },
  {
    text: "I'm so bad, but I'm so good at it",
    title: "Dumb Dumb",
    artist: "Somi",
  },
];

export default Server(() => {
  const app = express();

  app.use(express.json());
  app.use(
    cors({
      origin: "http://localhost:3000",
    })
  );
  app.options("*", cors());
  app.use(function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header(
      "Access-Control-Allow-Headers",
      "Origin, X-Requested-With, Content-Type, Accept"
    );
    next();
  });

  app.get("/db", (req, res) => {
    res.json(test);
  });

  app.post("/db/moment", (req, res) => {
    const { code } = req.body;

    // Use code to find content in db

    res.json({ code, ...moment });
  });

  app.post("/db/update", (req: Request<any, any, typeof test>, res) => {
    test = req.body;

    res.json(test);
  });

  app.post("/db/my", (req, res) => {
    const user = req.body;
    // if (user)
    // if (req.body)
    res.json(myMoments);
  });

  app.post("/db/set-code", (req, res) => {
    res.json({ status: "activated", ...req.body });
  });
  // app.post("/db/set-code", (req, res) => {
  //   // console.log("🚀 ~ app.get ~ content:", req);
  //   res.json(code);
  // });

  app.use(express.static("/dist"));

  return app.listen();
});
