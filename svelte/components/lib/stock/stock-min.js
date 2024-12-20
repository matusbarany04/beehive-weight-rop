/* sockjs-client v1.4.0 | http://sockjs.org | MIT license */
!(function (t) {
  if ("object" == typeof exports && "undefined" != typeof module)
    module.exports = t();
  else if ("function" == typeof define && define.amd) define([], t);
  else {
    ("undefined" != typeof window
      ? window
      : "undefined" != typeof global
      ? global
      : "undefined" != typeof self
      ? self
      : this
    ).SockJS = t();
  }
})(function () {
  return (function i(s, a, l) {
    function c(e, t) {
      if (!a[e]) {
        if (!s[e]) {
          var n = "function" == typeof require && require;
          if (!t && n) return n(e, !0);
          if (u) return u(e, !0);
          var r = new Error("Cannot find module '" + e + "'");
          throw ((r.code = "MODULE_NOT_FOUND"), r);
        }
        var o = (a[e] = { exports: {} });
        s[e][0].call(
          o.exports,
          function (t) {
            return c(s[e][1][t] || t);
          },
          o,
          o.exports,
          i,
          s,
          a,
          l,
        );
      }
      return a[e].exports;
    }

    for (
      var u = "function" == typeof require && require, t = 0;
      t < l.length;
      t++
    )
      c(l[t]);
    return c;
  })(
    {
      1: [
        function (n, r, t) {
          (function (t) {
            "use strict";
            var e = n("./transport-list");
            (r.exports = n("./main")(e)),
              "_sockjs_onload" in t && setTimeout(t._sockjs_onload, 1);
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        { "./main": 14, "./transport-list": 16 },
      ],
      2: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            o = t("./event");

          function i() {
            o.call(this),
              this.initEvent("close", !1, !1),
              (this.wasClean = !1),
              (this.code = 0),
              (this.reason = "");
          }

          r(i, o), (e.exports = i);
        },
        { "./event": 4, inherits: 54 },
      ],
      3: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            o = t("./eventtarget");

          function i() {
            o.call(this);
          }

          r(i, o),
            (i.prototype.removeAllListeners = function (t) {
              t ? delete this._listeners[t] : (this._listeners = {});
            }),
            (i.prototype.once = function (e, n) {
              var r = this,
                o = !1;
              this.on(e, function t() {
                r.removeListener(e, t),
                  o || ((o = !0), n.apply(this, arguments));
              });
            }),
            (i.prototype.emit = function () {
              var t = arguments[0],
                e = this._listeners[t];
              if (e) {
                for (
                  var n = arguments.length, r = new Array(n - 1), o = 1;
                  o < n;
                  o++
                )
                  r[o - 1] = arguments[o];
                for (var i = 0; i < e.length; i++) e[i].apply(this, r);
              }
            }),
            (i.prototype.on = i.prototype.addListener =
              o.prototype.addEventListener),
            (i.prototype.removeListener = o.prototype.removeEventListener),
            (e.exports.EventEmitter = i);
        },
        { "./eventtarget": 5, inherits: 54 },
      ],
      4: [
        function (t, e, n) {
          "use strict";

          function r(t) {
            this.type = t;
          }

          (r.prototype.initEvent = function (t, e, n) {
            return (
              (this.type = t),
              (this.bubbles = e),
              (this.cancelable = n),
              (this.timeStamp = +new Date()),
              this
            );
          }),
            (r.prototype.stopPropagation = function () {}),
            (r.prototype.preventDefault = function () {}),
            (r.CAPTURING_PHASE = 1),
            (r.AT_TARGET = 2),
            (r.BUBBLING_PHASE = 3),
            (e.exports = r);
        },
        {},
      ],
      5: [
        function (t, e, n) {
          "use strict";

          function r() {
            this._listeners = {};
          }

          (r.prototype.addEventListener = function (t, e) {
            t in this._listeners || (this._listeners[t] = []);
            var n = this._listeners[t];
            -1 === n.indexOf(e) && (n = n.concat([e])),
              (this._listeners[t] = n);
          }),
            (r.prototype.removeEventListener = function (t, e) {
              var n = this._listeners[t];
              if (n) {
                var r = n.indexOf(e);
                -1 === r ||
                  (1 < n.length
                    ? (this._listeners[t] = n
                        .slice(0, r)
                        .concat(n.slice(r + 1)))
                    : delete this._listeners[t]);
              }
            }),
            (r.prototype.dispatchEvent = function () {
              var t = arguments[0],
                e = t.type,
                n = 1 === arguments.length ? [t] : Array.apply(null, arguments);
              if (
                (this["on" + e] && this["on" + e].apply(this, n),
                e in this._listeners)
              )
                for (var r = this._listeners[e], o = 0; o < r.length; o++)
                  r[o].apply(this, n);
            }),
            (e.exports = r);
        },
        {},
      ],
      6: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            o = t("./event");

          function i(t) {
            o.call(this), this.initEvent("message", !1, !1), (this.data = t);
          }

          r(i, o), (e.exports = i);
        },
        { "./event": 4, inherits: 54 },
      ],
      7: [
        function (t, e, n) {
          "use strict";
          var r = t("json3"),
            o = t("./utils/iframe");

          function i(t) {
            (this._transport = t).on(
              "message",
              this._transportMessage.bind(this),
            ),
              t.on("close", this._transportClose.bind(this));
          }

          (i.prototype._transportClose = function (t, e) {
            o.postMessage("c", r.stringify([t, e]));
          }),
            (i.prototype._transportMessage = function (t) {
              o.postMessage("t", t);
            }),
            (i.prototype._send = function (t) {
              this._transport.send(t);
            }),
            (i.prototype._close = function () {
              this._transport.close(), this._transport.removeAllListeners();
            }),
            (e.exports = i);
        },
        { "./utils/iframe": 47, json3: 55 },
      ],
      8: [
        function (t, e, n) {
          "use strict";
          var f = t("./utils/url"),
            r = t("./utils/event"),
            h = t("json3"),
            d = t("./facade"),
            o = t("./info-iframe-receiver"),
            p = t("./utils/iframe"),
            m = t("./location"),
            v = function () {};
          e.exports = function (l, t) {
            var c,
              u = {};
            t.forEach(function (t) {
              t.facadeTransport &&
                (u[t.facadeTransport.transportName] = t.facadeTransport);
            }),
              (u[o.transportName] = o),
              (l.bootstrap_iframe = function () {
                var a;
                p.currentWindowId = m.hash.slice(1);
                r.attachEvent("message", function (e) {
                  if (
                    e.source === parent &&
                    (void 0 === c && (c = e.origin), e.origin === c)
                  ) {
                    var n;
                    try {
                      n = h.parse(e.data);
                    } catch (t) {
                      return void v("bad json", e.data);
                    }
                    if (n.windowId === p.currentWindowId)
                      switch (n.type) {
                        case "s":
                          var t;
                          try {
                            t = h.parse(n.data);
                          } catch (t) {
                            v("bad json", n.data);
                            break;
                          }
                          var r = t[0],
                            o = t[1],
                            i = t[2],
                            s = t[3];
                          if ((v(r, o, i, s), r !== l.version))
                            throw new Error(
                              'Incompatible SockJS! Main site uses: "' +
                                r +
                                '", the iframe: "' +
                                l.version +
                                '".',
                            );
                          if (
                            !f.isOriginEqual(i, m.href) ||
                            !f.isOriginEqual(s, m.href)
                          )
                            throw new Error(
                              "Can't connect to different domain from within an iframe. (" +
                                m.href +
                                ", " +
                                i +
                                ", " +
                                s +
                                ")",
                            );
                          a = new d(new u[o](i, s));
                          break;
                        case "m":
                          a._send(n.data);
                          break;
                        case "c":
                          a && a._close(), (a = null);
                      }
                  }
                }),
                  p.postMessage("s");
              });
          };
        },
        {
          "./facade": 7,
          "./info-iframe-receiver": 10,
          "./location": 13,
          "./utils/event": 46,
          "./utils/iframe": 47,
          "./utils/url": 52,
          debug: void 0,
          json3: 55,
        },
      ],
      9: [
        function (t, e, n) {
          "use strict";
          var r = t("events").EventEmitter,
            o = t("inherits"),
            s = t("json3"),
            a = t("./utils/object"),
            l = function () {};

          function i(t, e) {
            r.call(this);
            var o = this,
              i = +new Date();
            (this.xo = new e("GET", t)),
              this.xo.once("finish", function (t, e) {
                var n, r;
                if (200 === t) {
                  if (((r = +new Date() - i), e))
                    try {
                      n = s.parse(e);
                    } catch (t) {
                      l("bad json", e);
                    }
                  a.isObject(n) || (n = {});
                }
                o.emit("finish", n, r), o.removeAllListeners();
              });
          }

          o(i, r),
            (i.prototype.close = function () {
              this.removeAllListeners(), this.xo.close();
            }),
            (e.exports = i);
        },
        {
          "./utils/object": 49,
          debug: void 0,
          events: 3,
          inherits: 54,
          json3: 55,
        },
      ],
      10: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            o = t("events").EventEmitter,
            i = t("json3"),
            s = t("./transport/sender/xhr-local"),
            a = t("./info-ajax");

          function l(t) {
            var n = this;
            o.call(this),
              (this.ir = new a(t, s)),
              this.ir.once("finish", function (t, e) {
                (n.ir = null), n.emit("message", i.stringify([t, e]));
              });
          }

          r(l, o),
            (l.transportName = "iframe-info-receiver"),
            (l.prototype.close = function () {
              this.ir && (this.ir.close(), (this.ir = null)),
                this.removeAllListeners();
            }),
            (e.exports = l);
        },
        {
          "./info-ajax": 9,
          "./transport/sender/xhr-local": 37,
          events: 3,
          inherits: 54,
          json3: 55,
        },
      ],
      11: [
        function (n, o, t) {
          (function (r) {
            "use strict";
            var i = n("events").EventEmitter,
              t = n("inherits"),
              s = n("json3"),
              a = n("./utils/event"),
              l = n("./transport/iframe"),
              c = n("./info-iframe-receiver"),
              u = function () {};

            function e(e, n) {
              var o = this;
              i.call(this);

              function t() {
                var t = (o.ifr = new l(c.transportName, n, e));
                t.once("message", function (e) {
                  if (e) {
                    var t;
                    try {
                      t = s.parse(e);
                    } catch (t) {
                      return u("bad json", e), o.emit("finish"), void o.close();
                    }
                    var n = t[0],
                      r = t[1];
                    o.emit("finish", n, r);
                  }
                  o.close();
                }),
                  t.once("close", function () {
                    o.emit("finish"), o.close();
                  });
              }

              r.document.body ? t() : a.attachEvent("load", t);
            }

            t(e, i),
              (e.enabled = function () {
                return l.enabled();
              }),
              (e.prototype.close = function () {
                this.ifr && this.ifr.close(),
                  this.removeAllListeners(),
                  (this.ifr = null);
              }),
              (o.exports = e);
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        {
          "./info-iframe-receiver": 10,
          "./transport/iframe": 22,
          "./utils/event": 46,
          debug: void 0,
          events: 3,
          inherits: 54,
          json3: 55,
        },
      ],
      12: [
        function (t, e, n) {
          "use strict";
          var r = t("events").EventEmitter,
            o = t("inherits"),
            i = t("./utils/url"),
            s = t("./transport/sender/xdr"),
            a = t("./transport/sender/xhr-cors"),
            l = t("./transport/sender/xhr-local"),
            c = t("./transport/sender/xhr-fake"),
            u = t("./info-iframe"),
            f = t("./info-ajax"),
            h = function () {};

          function d(t, e) {
            h(t);
            var n = this;
            r.call(this),
              setTimeout(function () {
                n.doXhr(t, e);
              }, 0);
          }

          o(d, r),
            (d._getReceiver = function (t, e, n) {
              return n.sameOrigin
                ? new f(e, l)
                : a.enabled
                ? new f(e, a)
                : s.enabled && n.sameScheme
                ? new f(e, s)
                : u.enabled()
                ? new u(t, e)
                : new f(e, c);
            }),
            (d.prototype.doXhr = function (t, e) {
              var n = this,
                r = i.addPath(t, "/info");
              h("doXhr", r),
                (this.xo = d._getReceiver(t, r, e)),
                (this.timeoutRef = setTimeout(function () {
                  h("timeout"), n._cleanup(!1), n.emit("finish");
                }, d.timeout)),
                this.xo.once("finish", function (t, e) {
                  h("finish", t, e), n._cleanup(!0), n.emit("finish", t, e);
                });
            }),
            (d.prototype._cleanup = function (t) {
              h("_cleanup"),
                clearTimeout(this.timeoutRef),
                (this.timeoutRef = null),
                !t && this.xo && this.xo.close(),
                (this.xo = null);
            }),
            (d.prototype.close = function () {
              h("close"), this.removeAllListeners(), this._cleanup(!1);
            }),
            (d.timeout = 8e3),
            (e.exports = d);
        },
        {
          "./info-ajax": 9,
          "./info-iframe": 11,
          "./transport/sender/xdr": 34,
          "./transport/sender/xhr-cors": 35,
          "./transport/sender/xhr-fake": 36,
          "./transport/sender/xhr-local": 37,
          "./utils/url": 52,
          debug: void 0,
          events: 3,
          inherits: 54,
        },
      ],
      13: [
        function (t, e, n) {
          (function (t) {
            "use strict";
            e.exports = t.location || {
              origin: "http://localhost:80",
              protocol: "http:",
              host: "localhost",
              port: 80,
              href: "http://localhost/",
              hash: "",
            };
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        {},
      ],
      14: [
        function (_, E, t) {
          (function (i) {
            "use strict";
            _("./shims");
            var r,
              l = _("url-parse"),
              t = _("inherits"),
              s = _("json3"),
              c = _("./utils/random"),
              e = _("./utils/escape"),
              u = _("./utils/url"),
              a = _("./utils/event"),
              n = _("./utils/transport"),
              o = _("./utils/object"),
              f = _("./utils/browser"),
              h = _("./utils/log"),
              d = _("./event/event"),
              p = _("./event/eventtarget"),
              m = _("./location"),
              v = _("./event/close"),
              b = _("./event/trans-message"),
              y = _("./info-receiver"),
              g = function () {};

            function w(t, e, n) {
              if (!(this instanceof w)) return new w(t, e, n);
              if (arguments.length < 1)
                throw new TypeError(
                  "Failed to construct 'SockJS: 1 argument required, but only 0 present",
                );
              p.call(this),
                (this.readyState = w.CONNECTING),
                (this.extensions = ""),
                (this.protocol = ""),
                (n = n || {}).protocols_whitelist &&
                  h.warn(
                    "'protocols_whitelist' is DEPRECATED. Use 'transports' instead.",
                  ),
                (this._transportsWhitelist = n.transports),
                (this._transportOptions = n.transportOptions || {}),
                (this._timeout = n.timeout || 0);
              var r = n.sessionId || 8;
              if ("function" == typeof r) this._generateSessionId = r;
              else {
                if ("number" != typeof r)
                  throw new TypeError(
                    "If sessionId is used in the options, it needs to be a number or a function.",
                  );
                this._generateSessionId = function () {
                  return c.string(r);
                };
              }
              this._server = n.server || c.numberString(1e3);
              var o = new l(t);
              if (!o.host || !o.protocol)
                throw new SyntaxError("The URL '" + t + "' is invalid");
              if (o.hash)
                throw new SyntaxError("The URL must not contain a fragment");
              if ("http:" !== o.protocol && "https:" !== o.protocol)
                throw new SyntaxError(
                  "The URL's scheme must be either 'http:' or 'https:'. '" +
                    o.protocol +
                    "' is not allowed.",
                );
              var i = "https:" === o.protocol;
              if ("https:" === m.protocol && !i)
                throw new Error(
                  "SecurityError: An insecure SockJS connection may not be initiated from a page loaded over HTTPS",
                );
              e ? Array.isArray(e) || (e = [e]) : (e = []);
              var s = e.sort();
              s.forEach(function (t, e) {
                if (!t)
                  throw new SyntaxError(
                    "The protocols entry '" + t + "' is invalid.",
                  );
                if (e < s.length - 1 && t === s[e + 1])
                  throw new SyntaxError(
                    "The protocols entry '" + t + "' is duplicated.",
                  );
              });
              var a = u.getOrigin(m.href);
              (this._origin = a ? a.toLowerCase() : null),
                o.set("pathname", o.pathname.replace(/\/+$/, "")),
                (this.url = o.href),
                g("using url", this.url),
                (this._urlInfo = {
                  nullOrigin: !f.hasDomain(),
                  sameOrigin: u.isOriginEqual(this.url, m.href),
                  sameScheme: u.isSchemeEqual(this.url, m.href),
                }),
                (this._ir = new y(this.url, this._urlInfo)),
                this._ir.once("finish", this._receiveInfo.bind(this));
            }

            function x(t) {
              return 1e3 === t || (3e3 <= t && t <= 4999);
            }

            t(w, p),
              (w.prototype.close = function (t, e) {
                if (t && !x(t))
                  throw new Error("InvalidAccessError: Invalid code");
                if (e && 123 < e.length)
                  throw new SyntaxError(
                    "reason argument has an invalid length",
                  );
                if (
                  this.readyState !== w.CLOSING &&
                  this.readyState !== w.CLOSED
                ) {
                  this._close(t || 1e3, e || "Normal closure", !0);
                }
              }),
              (w.prototype.send = function (t) {
                if (
                  ("string" != typeof t && (t = "" + t),
                  this.readyState === w.CONNECTING)
                )
                  throw new Error(
                    "InvalidStateError: The connection has not been established yet",
                  );
                this.readyState === w.OPEN && this._transport.send(e.quote(t));
              }),
              (w.version = _("./version")),
              (w.CONNECTING = 0),
              (w.OPEN = 1),
              (w.CLOSING = 2),
              (w.CLOSED = 3),
              (w.prototype._receiveInfo = function (t, e) {
                if ((g("_receiveInfo", e), (this._ir = null), t)) {
                  (this._rto = this.countRTO(e)),
                    (this._transUrl = t.base_url ? t.base_url : this.url),
                    (t = o.extend(t, this._urlInfo)),
                    g("info", t);
                  var n = r.filterToEnabled(this._transportsWhitelist, t);
                  (this._transports = n.main),
                    g(this._transports.length + " enabled transports"),
                    this._connect();
                } else this._close(1002, "Cannot connect to server");
              }),
              (w.prototype._connect = function () {
                for (
                  var t = this._transports.shift();
                  t;
                  t = this._transports.shift()
                ) {
                  if (
                    (g("attempt", t.transportName),
                    t.needBody &&
                      (!i.document.body ||
                        (void 0 !== i.document.readyState &&
                          "complete" !== i.document.readyState &&
                          "interactive" !== i.document.readyState)))
                  )
                    return (
                      g("waiting for body"),
                      this._transports.unshift(t),
                      void a.attachEvent("load", this._connect.bind(this))
                    );
                  var e = Math.max(
                    this._timeout,
                    this._rto * t.roundTrips || 5e3,
                  );
                  (this._transportTimeoutId = setTimeout(
                    this._transportTimeout.bind(this),
                    e,
                  )),
                    g("using timeout", e);
                  var n = u.addPath(
                      this._transUrl,
                      "/" + this._server + "/" + this._generateSessionId(),
                    ),
                    r = this._transportOptions[t.transportName];
                  g("transport url", n);
                  var o = new t(n, this._transUrl, r);
                  return (
                    o.on("message", this._transportMessage.bind(this)),
                    o.once("close", this._transportClose.bind(this)),
                    (o.transportName = t.transportName),
                    void (this._transport = o)
                  );
                }
                this._close(2e3, "All transports failed", !1);
              }),
              (w.prototype._transportTimeout = function () {
                g("_transportTimeout"),
                  this.readyState === w.CONNECTING &&
                    (this._transport && this._transport.close(),
                    this._transportClose(2007, "Transport timed out"));
              }),
              (w.prototype._transportMessage = function (t) {
                g("_transportMessage", t);
                var e,
                  n = this,
                  r = t.slice(0, 1),
                  o = t.slice(1);
                switch (r) {
                  case "o":
                    return void this._open();
                  case "h":
                    return (
                      this.dispatchEvent(new d("heartbeat")),
                      void g("heartbeat", this.transport)
                    );
                }
                if (o)
                  try {
                    e = s.parse(o);
                  } catch (t) {
                    g("bad json", o);
                  }
                if (void 0 !== e)
                  switch (r) {
                    case "a":
                      Array.isArray(e) &&
                        e.forEach(function (t) {
                          g("message", n.transport, t),
                            n.dispatchEvent(new b(t));
                        });
                      break;
                    case "m":
                      g("message", this.transport, e),
                        this.dispatchEvent(new b(e));
                      break;
                    case "c":
                      Array.isArray(e) &&
                        2 === e.length &&
                        this._close(e[0], e[1], !0);
                  }
                else g("empty payload", o);
              }),
              (w.prototype._transportClose = function (t, e) {
                g("_transportClose", this.transport, t, e),
                  this._transport &&
                    (this._transport.removeAllListeners(),
                    (this._transport = null),
                    (this.transport = null)),
                  x(t) || 2e3 === t || this.readyState !== w.CONNECTING
                    ? this._close(t, e)
                    : this._connect();
              }),
              (w.prototype._open = function () {
                g(
                  "_open",
                  this._transport && this._transport.transportName,
                  this.readyState,
                ),
                  this.readyState === w.CONNECTING
                    ? (this._transportTimeoutId &&
                        (clearTimeout(this._transportTimeoutId),
                        (this._transportTimeoutId = null)),
                      (this.readyState = w.OPEN),
                      (this.transport = this._transport.transportName),
                      this.dispatchEvent(new d("open")),
                      g("connected", this.transport))
                    : this._close(1006, "Server lost session");
              }),
              (w.prototype._close = function (e, n, r) {
                g("_close", this.transport, e, n, r, this.readyState);
                var o = !1;
                if (
                  (this._ir && ((o = !0), this._ir.close(), (this._ir = null)),
                  this._transport &&
                    (this._transport.close(),
                    (this._transport = null),
                    (this.transport = null)),
                  this.readyState === w.CLOSED)
                )
                  throw new Error(
                    "InvalidStateError: SockJS has already been closed",
                  );
                (this.readyState = w.CLOSING),
                  setTimeout(
                    function () {
                      (this.readyState = w.CLOSED),
                        o && this.dispatchEvent(new d("error"));
                      var t = new v("close");
                      (t.wasClean = r || !1),
                        (t.code = e || 1e3),
                        (t.reason = n),
                        this.dispatchEvent(t),
                        (this.onmessage = this.onclose = this.onerror = null),
                        g("disconnected");
                    }.bind(this),
                    0,
                  );
              }),
              (w.prototype.countRTO = function (t) {
                return 100 < t ? 4 * t : 300 + t;
              }),
              (E.exports = function (t) {
                return (r = n(t)), _("./iframe-bootstrap")(w, t), w;
              });
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        {
          "./event/close": 2,
          "./event/event": 4,
          "./event/eventtarget": 5,
          "./event/trans-message": 6,
          "./iframe-bootstrap": 8,
          "./info-receiver": 12,
          "./location": 13,
          "./shims": 15,
          "./utils/browser": 44,
          "./utils/escape": 45,
          "./utils/event": 46,
          "./utils/log": 48,
          "./utils/object": 49,
          "./utils/random": 50,
          "./utils/transport": 51,
          "./utils/url": 52,
          "./version": 53,
          debug: void 0,
          inherits: 54,
          json3: 55,
          "url-parse": 58,
        },
      ],
      15: [
        function (t, e, n) {
          "use strict";

          function a(t) {
            return "[object Function]" === i.toString.call(t);
          }

          function l(t) {
            return "[object String]" === f.call(t);
          }

          var o,
            u = Array.prototype,
            i = Object.prototype,
            r = Function.prototype,
            s = String.prototype,
            c = u.slice,
            f = i.toString,
            h =
              Object.defineProperty &&
              (function () {
                try {
                  return Object.defineProperty({}, "x", {}), !0;
                } catch (t) {
                  return !1;
                }
              })();
          o = h
            ? function (t, e, n, r) {
                (!r && e in t) ||
                  Object.defineProperty(t, e, {
                    configurable: !0,
                    enumerable: !1,
                    writable: !0,
                    value: n,
                  });
              }
            : function (t, e, n, r) {
                (!r && e in t) || (t[e] = n);
              };

          function d(t, e, n) {
            for (var r in e) i.hasOwnProperty.call(e, r) && o(t, r, e[r], n);
          }

          function p(t) {
            if (null == t)
              throw new TypeError("can't convert " + t + " to object");
            return Object(t);
          }

          function m() {}

          d(r, {
            bind: function (e) {
              var n = this;
              if (!a(n))
                throw new TypeError(
                  "Function.prototype.bind called on incompatible " + n,
                );
              for (
                var r = c.call(arguments, 1),
                  t = Math.max(0, n.length - r.length),
                  o = [],
                  i = 0;
                i < t;
                i++
              )
                o.push("$" + i);
              var s = Function(
                "binder",
                "return function (" +
                  o.join(",") +
                  "){ return binder.apply(this, arguments); }",
              )(function () {
                if (this instanceof s) {
                  var t = n.apply(this, r.concat(c.call(arguments)));
                  return Object(t) === t ? t : this;
                }
                return n.apply(e, r.concat(c.call(arguments)));
              });
              return (
                n.prototype &&
                  ((m.prototype = n.prototype),
                  (s.prototype = new m()),
                  (m.prototype = null)),
                s
              );
            },
          }),
            d(Array, {
              isArray: function (t) {
                return "[object Array]" === f.call(t);
              },
            });
          var v,
            b,
            y,
            g = Object("a"),
            w = "a" !== g[0] || !(0 in g);
          d(
            u,
            {
              forEach: function (t, e) {
                var n = p(this),
                  r = w && l(this) ? this.split("") : n,
                  o = e,
                  i = -1,
                  s = r.length >>> 0;
                if (!a(t)) throw new TypeError();
                for (; ++i < s; ) i in r && t.call(o, r[i], i, n);
              },
            },
            ((v = u.forEach),
            (y = b = !0),
            v &&
              (v.call("foo", function (t, e, n) {
                "object" != typeof n && (b = !1);
              }),
              v.call(
                [1],
                function () {
                  y = "string" == typeof this;
                },
                "x",
              )),
            !(v && b && y)),
          );
          var x = Array.prototype.indexOf && -1 !== [0, 1].indexOf(1, 2);
          d(
            u,
            {
              indexOf: function (t, e) {
                var n = w && l(this) ? this.split("") : p(this),
                  r = n.length >>> 0;
                if (!r) return -1;
                var o = 0;
                for (
                  1 < arguments.length &&
                    (o = (function (t) {
                      var e = +t;
                      return (
                        e != e
                          ? (e = 0)
                          : 0 !== e &&
                            e !== 1 / 0 &&
                            e !== -1 / 0 &&
                            (e = (0 < e || -1) * Math.floor(Math.abs(e))),
                        e
                      );
                    })(e)),
                    o = 0 <= o ? o : Math.max(0, r + o);
                  o < r;
                  o++
                )
                  if (o in n && n[o] === t) return o;
                return -1;
              },
            },
            x,
          );
          var _,
            E = s.split;
          2 !== "ab".split(/(?:ab)*/).length ||
          4 !== ".".split(/(.?)(.?)/).length ||
          "t" === "tesst".split(/(s)*/)[1] ||
          4 !== "test".split(/(?:)/, -1).length ||
          "".split(/.?/).length ||
          1 < ".".split(/()()/).length
            ? ((_ = void 0 === /()??/.exec("")[1]),
              (s.split = function (t, e) {
                var n = this;
                if (void 0 === t && 0 === e) return [];
                if ("[object RegExp]" !== f.call(t)) return E.call(this, t, e);
                var r,
                  o,
                  i,
                  s,
                  a = [],
                  l =
                    (t.ignoreCase ? "i" : "") +
                    (t.multiline ? "m" : "") +
                    (t.extended ? "x" : "") +
                    (t.sticky ? "y" : ""),
                  c = 0;
                for (
                  t = new RegExp(t.source, l + "g"),
                    n += "",
                    _ || (r = new RegExp("^" + t.source + "$(?!\\s)", l)),
                    e =
                      void 0 === e
                        ? -1 >>> 0
                        : (function (t) {
                            return t >>> 0;
                          })(e);
                  (o = t.exec(n)) &&
                  !(
                    c < (i = o.index + o[0].length) &&
                    (a.push(n.slice(c, o.index)),
                    !_ &&
                      1 < o.length &&
                      o[0].replace(r, function () {
                        for (var t = 1; t < arguments.length - 2; t++)
                          void 0 === arguments[t] && (o[t] = void 0);
                      }),
                    1 < o.length &&
                      o.index < n.length &&
                      u.push.apply(a, o.slice(1)),
                    (s = o[0].length),
                    (c = i),
                    a.length >= e)
                  );

                )
                  t.lastIndex === o.index && t.lastIndex++;
                return (
                  c === n.length
                    ? (!s && t.test("")) || a.push("")
                    : a.push(n.slice(c)),
                  a.length > e ? a.slice(0, e) : a
                );
              }))
            : "0".split(void 0, 0).length &&
              (s.split = function (t, e) {
                return void 0 === t && 0 === e ? [] : E.call(this, t, e);
              });
          var j = s.substr,
            S = "".substr && "b" !== "0b".substr(-1);
          d(
            s,
            {
              substr: function (t, e) {
                return j.call(
                  this,
                  t < 0 && (t = this.length + t) < 0 ? 0 : t,
                  e,
                );
              },
            },
            S,
          );
        },
        {},
      ],
      16: [
        function (t, e, n) {
          "use strict";
          e.exports = [
            t("./transport/websocket"),
            t("./transport/xhr-streaming"),
            t("./transport/xdr-streaming"),
            t("./transport/eventsource"),
            t("./transport/lib/iframe-wrap")(t("./transport/eventsource")),
            t("./transport/htmlfile"),
            t("./transport/lib/iframe-wrap")(t("./transport/htmlfile")),
            t("./transport/xhr-polling"),
            t("./transport/xdr-polling"),
            t("./transport/lib/iframe-wrap")(t("./transport/xhr-polling")),
            t("./transport/jsonp-polling"),
          ];
        },
        {
          "./transport/eventsource": 20,
          "./transport/htmlfile": 21,
          "./transport/jsonp-polling": 23,
          "./transport/lib/iframe-wrap": 26,
          "./transport/websocket": 38,
          "./transport/xdr-polling": 39,
          "./transport/xdr-streaming": 40,
          "./transport/xhr-polling": 41,
          "./transport/xhr-streaming": 42,
        },
      ],
      17: [
        function (o, f, t) {
          (function (t) {
            "use strict";
            var i = o("events").EventEmitter,
              e = o("inherits"),
              s = o("../../utils/event"),
              a = o("../../utils/url"),
              l = t.XMLHttpRequest,
              c = function () {};

            function u(t, e, n, r) {
              c(t, e);
              var o = this;
              i.call(this),
                setTimeout(function () {
                  o._start(t, e, n, r);
                }, 0);
            }

            e(u, i),
              (u.prototype._start = function (t, e, n, r) {
                var o = this;
                try {
                  this.xhr = new l();
                } catch (t) {}
                if (!this.xhr)
                  return (
                    c("no xhr"),
                    this.emit("finish", 0, "no xhr support"),
                    void this._cleanup()
                  );
                (e = a.addQuery(e, "t=" + +new Date())),
                  (this.unloadRef = s.unloadAdd(function () {
                    c("unload cleanup"), o._cleanup(!0);
                  }));
                try {
                  this.xhr.open(t, e, !0),
                    this.timeout &&
                      "timeout" in this.xhr &&
                      ((this.xhr.timeout = this.timeout),
                      (this.xhr.ontimeout = function () {
                        c("xhr timeout"),
                          o.emit("finish", 0, ""),
                          o._cleanup(!1);
                      }));
                } catch (t) {
                  return (
                    c("exception", t),
                    this.emit("finish", 0, ""),
                    void this._cleanup(!1)
                  );
                }
                if (
                  ((r && r.noCredentials) ||
                    !u.supportsCORS ||
                    (c("withCredentials"), (this.xhr.withCredentials = !0)),
                  r && r.headers)
                )
                  for (var i in r.headers)
                    this.xhr.setRequestHeader(i, r.headers[i]);
                this.xhr.onreadystatechange = function () {
                  if (o.xhr) {
                    var t,
                      e,
                      n = o.xhr;
                    switch ((c("readyState", n.readyState), n.readyState)) {
                      case 3:
                        try {
                          (e = n.status), (t = n.responseText);
                        } catch (t) {}
                        c("status", e),
                          1223 === e && (e = 204),
                          200 === e &&
                            t &&
                            0 < t.length &&
                            (c("chunk"), o.emit("chunk", e, t));
                        break;
                      case 4:
                        (e = n.status),
                          c("status", e),
                          1223 === e && (e = 204),
                          (12005 !== e && 12029 !== e) || (e = 0),
                          c("finish", e, n.responseText),
                          o.emit("finish", e, n.responseText),
                          o._cleanup(!1);
                    }
                  }
                };
                try {
                  o.xhr.send(n);
                } catch (t) {
                  o.emit("finish", 0, ""), o._cleanup(!1);
                }
              }),
              (u.prototype._cleanup = function (t) {
                if ((c("cleanup"), this.xhr)) {
                  if (
                    (this.removeAllListeners(),
                    s.unloadDel(this.unloadRef),
                    (this.xhr.onreadystatechange = function () {}),
                    this.xhr.ontimeout && (this.xhr.ontimeout = null),
                    t)
                  )
                    try {
                      this.xhr.abort();
                    } catch (t) {}
                  this.unloadRef = this.xhr = null;
                }
              }),
              (u.prototype.close = function () {
                c("close"), this._cleanup(!0);
              }),
              (u.enabled = !!l);
            var n = ["Active"].concat("Object").join("X");
            !u.enabled &&
              n in t &&
              (c("overriding xmlhttprequest"),
              (u.enabled = !!new (l = function () {
                try {
                  return new t[n]("Microsoft.XMLHTTP");
                } catch (t) {
                  return null;
                }
              })()));
            var r = !1;
            try {
              r = "withCredentials" in new l();
            } catch (t) {}
            (u.supportsCORS = r), (f.exports = u);
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        {
          "../../utils/event": 46,
          "../../utils/url": 52,
          debug: void 0,
          events: 3,
          inherits: 54,
        },
      ],
      18: [
        function (t, e, n) {
          (function (t) {
            e.exports = t.EventSource;
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        {},
      ],
      19: [
        function (t, n, e) {
          (function (t) {
            "use strict";
            var e = t.WebSocket || t.MozWebSocket;
            n.exports = e
              ? function (t) {
                  return new e(t);
                }
              : void 0;
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        {},
      ],
      20: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            o = t("./lib/ajax-based"),
            i = t("./receiver/eventsource"),
            s = t("./sender/xhr-cors"),
            a = t("eventsource");

          function l(t) {
            if (!l.enabled())
              throw new Error("Transport created when disabled");
            o.call(this, t, "/eventsource", i, s);
          }

          r(l, o),
            (l.enabled = function () {
              return !!a;
            }),
            (l.transportName = "eventsource"),
            (l.roundTrips = 2),
            (e.exports = l);
        },
        {
          "./lib/ajax-based": 24,
          "./receiver/eventsource": 29,
          "./sender/xhr-cors": 35,
          eventsource: 18,
          inherits: 54,
        },
      ],
      21: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            o = t("./receiver/htmlfile"),
            i = t("./sender/xhr-local"),
            s = t("./lib/ajax-based");

          function a(t) {
            if (!o.enabled) throw new Error("Transport created when disabled");
            s.call(this, t, "/htmlfile", o, i);
          }

          r(a, s),
            (a.enabled = function (t) {
              return o.enabled && t.sameOrigin;
            }),
            (a.transportName = "htmlfile"),
            (a.roundTrips = 2),
            (e.exports = a);
        },
        {
          "./lib/ajax-based": 24,
          "./receiver/htmlfile": 30,
          "./sender/xhr-local": 37,
          inherits: 54,
        },
      ],
      22: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            o = t("json3"),
            i = t("events").EventEmitter,
            s = t("../version"),
            a = t("../utils/url"),
            l = t("../utils/iframe"),
            c = t("../utils/event"),
            u = t("../utils/random"),
            f = function () {};

          function h(t, e, n) {
            if (!h.enabled())
              throw new Error("Transport created when disabled");
            i.call(this);
            var r = this;
            (this.origin = a.getOrigin(n)),
              (this.baseUrl = n),
              (this.transUrl = e),
              (this.transport = t),
              (this.windowId = u.string(8));
            var o = a.addPath(n, "/iframe.html") + "#" + this.windowId;
            f(t, e, o),
              (this.iframeObj = l.createIframe(o, function (t) {
                f("err callback"),
                  r.emit("close", 1006, "Unable to load an iframe (" + t + ")"),
                  r.close();
              })),
              (this.onmessageCallback = this._message.bind(this)),
              c.attachEvent("message", this.onmessageCallback);
          }

          r(h, i),
            (h.prototype.close = function () {
              if ((f("close"), this.removeAllListeners(), this.iframeObj)) {
                c.detachEvent("message", this.onmessageCallback);
                try {
                  this.postMessage("c");
                } catch (t) {}
                this.iframeObj.cleanup(),
                  (this.iframeObj = null),
                  (this.onmessageCallback = this.iframeObj = null);
              }
            }),
            (h.prototype._message = function (e) {
              if (
                (f("message", e.data), a.isOriginEqual(e.origin, this.origin))
              ) {
                var n;
                try {
                  n = o.parse(e.data);
                } catch (t) {
                  return void f("bad json", e.data);
                }
                if (n.windowId === this.windowId)
                  switch (n.type) {
                    case "s":
                      this.iframeObj.loaded(),
                        this.postMessage(
                          "s",
                          o.stringify([
                            s,
                            this.transport,
                            this.transUrl,
                            this.baseUrl,
                          ]),
                        );
                      break;
                    case "t":
                      this.emit("message", n.data);
                      break;
                    case "c":
                      var t;
                      try {
                        t = o.parse(n.data);
                      } catch (t) {
                        return void f("bad json", n.data);
                      }
                      this.emit("close", t[0], t[1]), this.close();
                  }
                else f("mismatched window id", n.windowId, this.windowId);
              } else f("not same origin", e.origin, this.origin);
            }),
            (h.prototype.postMessage = function (t, e) {
              f("postMessage", t, e),
                this.iframeObj.post(
                  o.stringify({
                    windowId: this.windowId,
                    type: t,
                    data: e || "",
                  }),
                  this.origin,
                );
            }),
            (h.prototype.send = function (t) {
              f("send", t), this.postMessage("m", t);
            }),
            (h.enabled = function () {
              return l.iframeEnabled;
            }),
            (h.transportName = "iframe"),
            (h.roundTrips = 2),
            (e.exports = h);
        },
        {
          "../utils/event": 46,
          "../utils/iframe": 47,
          "../utils/random": 50,
          "../utils/url": 52,
          "../version": 53,
          debug: void 0,
          events: 3,
          inherits: 54,
          json3: 55,
        },
      ],
      23: [
        function (s, a, t) {
          (function (t) {
            "use strict";
            var e = s("inherits"),
              n = s("./lib/sender-receiver"),
              r = s("./receiver/jsonp"),
              o = s("./sender/jsonp");

            function i(t) {
              if (!i.enabled())
                throw new Error("Transport created when disabled");
              n.call(this, t, "/jsonp", o, r);
            }

            e(i, n),
              (i.enabled = function () {
                return !!t.document;
              }),
              (i.transportName = "jsonp-polling"),
              (i.roundTrips = 1),
              (i.needBody = !0),
              (a.exports = i);
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        {
          "./lib/sender-receiver": 28,
          "./receiver/jsonp": 31,
          "./sender/jsonp": 33,
          inherits: 54,
        },
      ],
      24: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            a = t("../../utils/url"),
            o = t("./sender-receiver"),
            l = function () {};

          function i(t, e, n, r) {
            o.call(
              this,
              t,
              e,
              (function (s) {
                return function (t, e, n) {
                  l("create ajax sender", t, e);
                  var r = {};
                  "string" == typeof e &&
                    (r.headers = { "Content-type": "text/plain" });
                  var o = a.addPath(t, "/xhr_send"),
                    i = new s("POST", o, e, r);
                  return (
                    i.once("finish", function (t) {
                      if ((l("finish", t), (i = null), 200 !== t && 204 !== t))
                        return n(new Error("http status " + t));
                      n();
                    }),
                    function () {
                      l("abort"), i.close(), (i = null);
                      var t = new Error("Aborted");
                      (t.code = 1e3), n(t);
                    }
                  );
                };
              })(r),
              n,
              r,
            );
          }

          r(i, o), (e.exports = i);
        },
        {
          "../../utils/url": 52,
          "./sender-receiver": 28,
          debug: void 0,
          inherits: 54,
        },
      ],
      25: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            o = t("events").EventEmitter,
            i = function () {};

          function s(t, e) {
            i(t),
              o.call(this),
              (this.sendBuffer = []),
              (this.sender = e),
              (this.url = t);
          }

          r(s, o),
            (s.prototype.send = function (t) {
              i("send", t),
                this.sendBuffer.push(t),
                this.sendStop || this.sendSchedule();
            }),
            (s.prototype.sendScheduleWait = function () {
              i("sendScheduleWait");
              var t,
                e = this;
              (this.sendStop = function () {
                i("sendStop"), (e.sendStop = null), clearTimeout(t);
              }),
                (t = setTimeout(function () {
                  i("timeout"), (e.sendStop = null), e.sendSchedule();
                }, 25));
            }),
            (s.prototype.sendSchedule = function () {
              i("sendSchedule", this.sendBuffer.length);
              var e = this;
              if (0 < this.sendBuffer.length) {
                var t = "[" + this.sendBuffer.join(",") + "]";
                (this.sendStop = this.sender(this.url, t, function (t) {
                  (e.sendStop = null),
                    t
                      ? (i("error", t),
                        e.emit("close", t.code || 1006, "Sending error: " + t),
                        e.close())
                      : e.sendScheduleWait();
                })),
                  (this.sendBuffer = []);
              }
            }),
            (s.prototype._cleanup = function () {
              i("_cleanup"), this.removeAllListeners();
            }),
            (s.prototype.close = function () {
              i("close"),
                this._cleanup(),
                this.sendStop && (this.sendStop(), (this.sendStop = null));
            }),
            (e.exports = s);
        },
        { debug: void 0, events: 3, inherits: 54 },
      ],
      26: [
        function (t, n, e) {
          (function (o) {
            "use strict";
            var e = t("inherits"),
              i = t("../iframe"),
              s = t("../../utils/object");
            n.exports = function (r) {
              function t(t, e) {
                i.call(this, r.transportName, t, e);
              }

              return (
                e(t, i),
                (t.enabled = function (t, e) {
                  if (!o.document) return !1;
                  var n = s.extend({}, e);
                  return (n.sameOrigin = !0), r.enabled(n) && i.enabled();
                }),
                (t.transportName = "iframe-" + r.transportName),
                (t.needBody = !0),
                (t.roundTrips = i.roundTrips + r.roundTrips - 1),
                (t.facadeTransport = r),
                t
              );
            };
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        { "../../utils/object": 49, "../iframe": 22, inherits: 54 },
      ],
      27: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            o = t("events").EventEmitter,
            i = function () {};

          function s(t, e, n) {
            i(e),
              o.call(this),
              (this.Receiver = t),
              (this.receiveUrl = e),
              (this.AjaxObject = n),
              this._scheduleReceiver();
          }

          r(s, o),
            (s.prototype._scheduleReceiver = function () {
              i("_scheduleReceiver");
              var n = this,
                r = (this.poll = new this.Receiver(
                  this.receiveUrl,
                  this.AjaxObject,
                ));
              r.on("message", function (t) {
                i("message", t), n.emit("message", t);
              }),
                r.once("close", function (t, e) {
                  i("close", t, e, n.pollIsClosing),
                    (n.poll = r = null),
                    n.pollIsClosing ||
                      ("network" === e
                        ? n._scheduleReceiver()
                        : (n.emit("close", t || 1006, e),
                          n.removeAllListeners()));
                });
            }),
            (s.prototype.abort = function () {
              i("abort"),
                this.removeAllListeners(),
                (this.pollIsClosing = !0),
                this.poll && this.poll.abort();
            }),
            (e.exports = s);
        },
        { debug: void 0, events: 3, inherits: 54 },
      ],
      28: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            a = t("../../utils/url"),
            l = t("./buffered-sender"),
            c = t("./polling"),
            u = function () {};

          function o(t, e, n, r, o) {
            var i = a.addPath(t, e);
            u(i);
            var s = this;
            l.call(this, t, n),
              (this.poll = new c(r, i, o)),
              this.poll.on("message", function (t) {
                u("poll message", t), s.emit("message", t);
              }),
              this.poll.once("close", function (t, e) {
                u("poll close", t, e),
                  (s.poll = null),
                  s.emit("close", t, e),
                  s.close();
              });
          }

          r(o, l),
            (o.prototype.close = function () {
              l.prototype.close.call(this),
                u("close"),
                this.removeAllListeners(),
                this.poll && (this.poll.abort(), (this.poll = null));
            }),
            (e.exports = o);
        },
        {
          "../../utils/url": 52,
          "./buffered-sender": 25,
          "./polling": 27,
          debug: void 0,
          inherits: 54,
        },
      ],
      29: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            o = t("events").EventEmitter,
            i = t("eventsource"),
            s = function () {};

          function a(t) {
            s(t), o.call(this);
            var n = this,
              r = (this.es = new i(t));
            (r.onmessage = function (t) {
              s("message", t.data), n.emit("message", decodeURI(t.data));
            }),
              (r.onerror = function (t) {
                s("error", r.readyState, t);
                var e = 2 !== r.readyState ? "network" : "permanent";
                n._cleanup(), n._close(e);
              });
          }

          r(a, o),
            (a.prototype.abort = function () {
              s("abort"), this._cleanup(), this._close("user");
            }),
            (a.prototype._cleanup = function () {
              s("cleanup");
              var t = this.es;
              t &&
                ((t.onmessage = t.onerror = null), t.close(), (this.es = null));
            }),
            (a.prototype._close = function (t) {
              s("close", t);
              var e = this;
              setTimeout(function () {
                e.emit("close", null, t), e.removeAllListeners();
              }, 200);
            }),
            (e.exports = a);
        },
        { debug: void 0, events: 3, eventsource: 18, inherits: 54 },
      ],
      30: [
        function (n, u, t) {
          (function (r) {
            "use strict";
            var t = n("inherits"),
              o = n("../../utils/iframe"),
              i = n("../../utils/url"),
              s = n("events").EventEmitter,
              a = n("../../utils/random"),
              l = function () {};

            function c(t) {
              l(t), s.call(this);
              var e = this;
              o.polluteGlobalNamespace(),
                (this.id = "a" + a.string(6)),
                (t = i.addQuery(
                  t,
                  "c=" + decodeURIComponent(o.WPrefix + "." + this.id),
                )),
                l("using htmlfile", c.htmlfileEnabled);
              var n = c.htmlfileEnabled ? o.createHtmlfile : o.createIframe;
              (r[o.WPrefix][this.id] = {
                start: function () {
                  l("start"), e.iframeObj.loaded();
                },
                message: function (t) {
                  l("message", t), e.emit("message", t);
                },
                stop: function () {
                  l("stop"), e._cleanup(), e._close("network");
                },
              }),
                (this.iframeObj = n(t, function () {
                  l("callback"), e._cleanup(), e._close("permanent");
                }));
            }

            t(c, s),
              (c.prototype.abort = function () {
                l("abort"), this._cleanup(), this._close("user");
              }),
              (c.prototype._cleanup = function () {
                l("_cleanup"),
                  this.iframeObj &&
                    (this.iframeObj.cleanup(), (this.iframeObj = null)),
                  delete r[o.WPrefix][this.id];
              }),
              (c.prototype._close = function (t) {
                l("_close", t),
                  this.emit("close", null, t),
                  this.removeAllListeners();
              }),
              (c.htmlfileEnabled = !1);
            var e = ["Active"].concat("Object").join("X");
            if (e in r)
              try {
                c.htmlfileEnabled = !!new r[e]("htmlfile");
              } catch (t) {}
            (c.enabled = c.htmlfileEnabled || o.iframeEnabled), (u.exports = c);
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        {
          "../../utils/iframe": 47,
          "../../utils/random": 50,
          "../../utils/url": 52,
          debug: void 0,
          events: 3,
          inherits: 54,
        },
      ],
      31: [
        function (e, n, t) {
          (function (i) {
            "use strict";
            var r = e("../../utils/iframe"),
              s = e("../../utils/random"),
              a = e("../../utils/browser"),
              o = e("../../utils/url"),
              t = e("inherits"),
              l = e("events").EventEmitter,
              c = function () {};

            function u(t) {
              c(t);
              var e = this;
              l.call(this),
                r.polluteGlobalNamespace(),
                (this.id = "a" + s.string(6));
              var n = o.addQuery(
                t,
                "c=" + encodeURIComponent(r.WPrefix + "." + this.id),
              );
              (i[r.WPrefix][this.id] = this._callback.bind(this)),
                this._createScript(n),
                (this.timeoutId = setTimeout(function () {
                  c("timeout"),
                    e._abort(
                      new Error("JSONP script loaded abnormally (timeout)"),
                    );
                }, u.timeout));
            }

            t(u, l),
              (u.prototype.abort = function () {
                if ((c("abort"), i[r.WPrefix][this.id])) {
                  var t = new Error("JSONP user aborted read");
                  (t.code = 1e3), this._abort(t);
                }
              }),
              (u.timeout = 35e3),
              (u.scriptErrorTimeout = 1e3),
              (u.prototype._callback = function (t) {
                c("_callback", t),
                  this._cleanup(),
                  this.aborting ||
                    (t && (c("message", t), this.emit("message", t)),
                    this.emit("close", null, "network"),
                    this.removeAllListeners());
              }),
              (u.prototype._abort = function (t) {
                c("_abort", t),
                  this._cleanup(),
                  (this.aborting = !0),
                  this.emit("close", t.code, t.message),
                  this.removeAllListeners();
              }),
              (u.prototype._cleanup = function () {
                if (
                  (c("_cleanup"),
                  clearTimeout(this.timeoutId),
                  this.script2 &&
                    (this.script2.parentNode.removeChild(this.script2),
                    (this.script2 = null)),
                  this.script)
                ) {
                  var t = this.script;
                  t.parentNode.removeChild(t),
                    (t.onreadystatechange =
                      t.onerror =
                      t.onload =
                      t.onclick =
                        null),
                    (this.script = null);
                }
                delete i[r.WPrefix][this.id];
              }),
              (u.prototype._scriptError = function () {
                c("_scriptError");
                var t = this;
                this.errorTimer ||
                  (this.errorTimer = setTimeout(function () {
                    t.loadedOkay ||
                      t._abort(
                        new Error("JSONP script loaded abnormally (onerror)"),
                      );
                  }, u.scriptErrorTimeout));
              }),
              (u.prototype._createScript = function (t) {
                c("_createScript", t);
                var e,
                  n = this,
                  r = (this.script = i.document.createElement("script"));
                if (
                  ((r.id = "a" + s.string(8)),
                  (r.src = t),
                  (r.type = "text/javascript"),
                  (r.charset = "UTF-8"),
                  (r.onerror = this._scriptError.bind(this)),
                  (r.onload = function () {
                    c("onload"),
                      n._abort(
                        new Error("JSONP script loaded abnormally (onload)"),
                      );
                  }),
                  (r.onreadystatechange = function () {
                    if (
                      (c("onreadystatechange", r.readyState),
                      /loaded|closed/.test(r.readyState))
                    ) {
                      if (r && r.htmlFor && r.onclick) {
                        n.loadedOkay = !0;
                        try {
                          r.onclick();
                        } catch (t) {}
                      }
                      r &&
                        n._abort(
                          new Error(
                            "JSONP script loaded abnormally (onreadystatechange)",
                          ),
                        );
                    }
                  }),
                  void 0 === r.async && i.document.attachEvent)
                )
                  if (a.isOpera())
                    ((e = this.script2 =
                      i.document.createElement("script")).text =
                      "try{var a = document.getElementById('" +
                      r.id +
                      "'); if(a)a.onerror();}catch(x){};"),
                      (r.async = e.async = !1);
                  else {
                    try {
                      (r.htmlFor = r.id), (r.event = "onclick");
                    } catch (t) {}
                    r.async = !0;
                  }
                void 0 !== r.async && (r.async = !0);
                var o = i.document.getElementsByTagName("head")[0];
                o.insertBefore(r, o.firstChild),
                  e && o.insertBefore(e, o.firstChild);
              }),
              (n.exports = u);
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        {
          "../../utils/browser": 44,
          "../../utils/iframe": 47,
          "../../utils/random": 50,
          "../../utils/url": 52,
          debug: void 0,
          events: 3,
          inherits: 54,
        },
      ],
      32: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            o = t("events").EventEmitter,
            i = function () {};

          function s(t, e) {
            i(t), o.call(this);
            var r = this;
            (this.bufferPosition = 0),
              (this.xo = new e("POST", t, null)),
              this.xo.on("chunk", this._chunkHandler.bind(this)),
              this.xo.once("finish", function (t, e) {
                i("finish", t, e), r._chunkHandler(t, e), (r.xo = null);
                var n = 200 === t ? "network" : "permanent";
                i("close", n), r.emit("close", null, n), r._cleanup();
              });
          }

          r(s, o),
            (s.prototype._chunkHandler = function (t, e) {
              if ((i("_chunkHandler", t), 200 === t && e))
                for (var n = -1; ; this.bufferPosition += n + 1) {
                  var r = e.slice(this.bufferPosition);
                  if (-1 === (n = r.indexOf("\n"))) break;
                  var o = r.slice(0, n);
                  o && (i("message", o), this.emit("message", o));
                }
            }),
            (s.prototype._cleanup = function () {
              i("_cleanup"), this.removeAllListeners();
            }),
            (s.prototype.abort = function () {
              i("abort"),
                this.xo &&
                  (this.xo.close(),
                  i("close"),
                  this.emit("close", null, "user"),
                  (this.xo = null)),
                this._cleanup();
            }),
            (e.exports = s);
        },
        { debug: void 0, events: 3, inherits: 54 },
      ],
      33: [
        function (t, e, n) {
          (function (s) {
            "use strict";
            var a,
              l,
              c = t("../../utils/random"),
              u = t("../../utils/url"),
              f = function () {};
            e.exports = function (t, e, n) {
              f(t, e),
                a ||
                  (f("createForm"),
                  ((a = s.document.createElement("form")).style.display =
                    "none"),
                  (a.style.position = "absolute"),
                  (a.method = "POST"),
                  (a.enctype = "application/x-www-form-urlencoded"),
                  (a.acceptCharset = "UTF-8"),
                  ((l = s.document.createElement("textarea")).name = "d"),
                  a.appendChild(l),
                  s.document.body.appendChild(a));
              var r = "a" + c.string(8);
              (a.target = r),
                (a.action = u.addQuery(u.addPath(t, "/jsonp_send"), "i=" + r));
              var o = (function (e) {
                f("createIframe", e);
                try {
                  return s.document.createElement('<iframe name="' + e + '">');
                } catch (t) {
                  var n = s.document.createElement("iframe");
                  return (n.name = e), n;
                }
              })(r);
              (o.id = r), (o.style.display = "none"), a.appendChild(o);
              try {
                l.value = e;
              } catch (t) {}
              a.submit();

              function i(t) {
                f("completed", r, t),
                  o.onerror &&
                    ((o.onreadystatechange = o.onerror = o.onload = null),
                    setTimeout(function () {
                      f("cleaning up", r),
                        o.parentNode.removeChild(o),
                        (o = null);
                    }, 500),
                    (l.value = ""),
                    n(t));
              }

              return (
                (o.onerror = function () {
                  f("onerror", r), i();
                }),
                (o.onload = function () {
                  f("onload", r), i();
                }),
                (o.onreadystatechange = function (t) {
                  f("onreadystatechange", r, o.readyState, t),
                    "complete" === o.readyState && i();
                }),
                function () {
                  f("aborted", r), i(new Error("Aborted"));
                }
              );
            };
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        { "../../utils/random": 50, "../../utils/url": 52, debug: void 0 },
      ],
      34: [
        function (r, c, t) {
          (function (i) {
            "use strict";
            var o = r("events").EventEmitter,
              t = r("inherits"),
              s = r("../../utils/event"),
              e = r("../../utils/browser"),
              a = r("../../utils/url"),
              l = function () {};

            function n(t, e, n) {
              l(t, e);
              var r = this;
              o.call(this),
                setTimeout(function () {
                  r._start(t, e, n);
                }, 0);
            }

            t(n, o),
              (n.prototype._start = function (t, e, n) {
                l("_start");
                var r = this,
                  o = new i.XDomainRequest();
                (e = a.addQuery(e, "t=" + +new Date())),
                  (o.onerror = function () {
                    l("onerror"), r._error();
                  }),
                  (o.ontimeout = function () {
                    l("ontimeout"), r._error();
                  }),
                  (o.onprogress = function () {
                    l("progress", o.responseText),
                      r.emit("chunk", 200, o.responseText);
                  }),
                  (o.onload = function () {
                    l("load"),
                      r.emit("finish", 200, o.responseText),
                      r._cleanup(!1);
                  }),
                  (this.xdr = o),
                  (this.unloadRef = s.unloadAdd(function () {
                    r._cleanup(!0);
                  }));
                try {
                  this.xdr.open(t, e),
                    this.timeout && (this.xdr.timeout = this.timeout),
                    this.xdr.send(n);
                } catch (t) {
                  this._error();
                }
              }),
              (n.prototype._error = function () {
                this.emit("finish", 0, ""), this._cleanup(!1);
              }),
              (n.prototype._cleanup = function (t) {
                if ((l("cleanup", t), this.xdr)) {
                  if (
                    (this.removeAllListeners(),
                    s.unloadDel(this.unloadRef),
                    (this.xdr.ontimeout =
                      this.xdr.onerror =
                      this.xdr.onprogress =
                      this.xdr.onload =
                        null),
                    t)
                  )
                    try {
                      this.xdr.abort();
                    } catch (t) {}
                  this.unloadRef = this.xdr = null;
                }
              }),
              (n.prototype.close = function () {
                l("close"), this._cleanup(!0);
              }),
              (n.enabled = !(!i.XDomainRequest || !e.hasDomain())),
              (c.exports = n);
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        {
          "../../utils/browser": 44,
          "../../utils/event": 46,
          "../../utils/url": 52,
          debug: void 0,
          events: 3,
          inherits: 54,
        },
      ],
      35: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            o = t("../driver/xhr");

          function i(t, e, n, r) {
            o.call(this, t, e, n, r);
          }

          r(i, o), (i.enabled = o.enabled && o.supportsCORS), (e.exports = i);
        },
        { "../driver/xhr": 17, inherits: 54 },
      ],
      36: [
        function (t, e, n) {
          "use strict";
          var r = t("events").EventEmitter;

          function o() {
            var t = this;
            r.call(this),
              (this.to = setTimeout(function () {
                t.emit("finish", 200, "{}");
              }, o.timeout));
          }

          t("inherits")(o, r),
            (o.prototype.close = function () {
              clearTimeout(this.to);
            }),
            (o.timeout = 2e3),
            (e.exports = o);
        },
        { events: 3, inherits: 54 },
      ],
      37: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            o = t("../driver/xhr");

          function i(t, e, n) {
            o.call(this, t, e, n, { noCredentials: !0 });
          }

          r(i, o), (i.enabled = o.enabled), (e.exports = i);
        },
        { "../driver/xhr": 17, inherits: 54 },
      ],
      38: [
        function (t, e, n) {
          "use strict";
          var i = t("../utils/event"),
            s = t("../utils/url"),
            r = t("inherits"),
            a = t("events").EventEmitter,
            l = t("./driver/websocket"),
            c = function () {};

          function u(t, e, n) {
            if (!u.enabled())
              throw new Error("Transport created when disabled");
            a.call(this), c("constructor", t);
            var r = this,
              o = s.addPath(t, "/websocket");
            (o =
              "https" === o.slice(0, 5)
                ? "wss" + o.slice(5)
                : "ws" + o.slice(4)),
              (this.url = o),
              (this.ws = new l(this.url, [], n)),
              (this.ws.onmessage = function (t) {
                c("message event", t.data), r.emit("message", t.data);
              }),
              (this.unloadRef = i.unloadAdd(function () {
                c("unload"), r.ws.close();
              })),
              (this.ws.onclose = function (t) {
                c("close event", t.code, t.reason),
                  r.emit("close", t.code, t.reason),
                  r._cleanup();
              }),
              (this.ws.onerror = function (t) {
                c("error event", t),
                  r.emit("close", 1006, "WebSocket connection broken"),
                  r._cleanup();
              });
          }

          r(u, a),
            (u.prototype.send = function (t) {
              var e = "[" + t + "]";
              c("send", e), this.ws.send(e);
            }),
            (u.prototype.close = function () {
              c("close");
              var t = this.ws;
              this._cleanup(), t && t.close();
            }),
            (u.prototype._cleanup = function () {
              c("_cleanup");
              var t = this.ws;
              t && (t.onmessage = t.onclose = t.onerror = null),
                i.unloadDel(this.unloadRef),
                (this.unloadRef = this.ws = null),
                this.removeAllListeners();
            }),
            (u.enabled = function () {
              return c("enabled"), !!l;
            }),
            (u.transportName = "websocket"),
            (u.roundTrips = 2),
            (e.exports = u);
        },
        {
          "../utils/event": 46,
          "../utils/url": 52,
          "./driver/websocket": 19,
          debug: void 0,
          events: 3,
          inherits: 54,
        },
      ],
      39: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            o = t("./lib/ajax-based"),
            i = t("./xdr-streaming"),
            s = t("./receiver/xhr"),
            a = t("./sender/xdr");

          function l(t) {
            if (!a.enabled) throw new Error("Transport created when disabled");
            o.call(this, t, "/xhr", s, a);
          }

          r(l, o),
            (l.enabled = i.enabled),
            (l.transportName = "xdr-polling"),
            (l.roundTrips = 2),
            (e.exports = l);
        },
        {
          "./lib/ajax-based": 24,
          "./receiver/xhr": 32,
          "./sender/xdr": 34,
          "./xdr-streaming": 40,
          inherits: 54,
        },
      ],
      40: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            o = t("./lib/ajax-based"),
            i = t("./receiver/xhr"),
            s = t("./sender/xdr");

          function a(t) {
            if (!s.enabled) throw new Error("Transport created when disabled");
            o.call(this, t, "/xhr_streaming", i, s);
          }

          r(a, o),
            (a.enabled = function (t) {
              return (
                !t.cookie_needed && !t.nullOrigin && s.enabled && t.sameScheme
              );
            }),
            (a.transportName = "xdr-streaming"),
            (a.roundTrips = 2),
            (e.exports = a);
        },
        {
          "./lib/ajax-based": 24,
          "./receiver/xhr": 32,
          "./sender/xdr": 34,
          inherits: 54,
        },
      ],
      41: [
        function (t, e, n) {
          "use strict";
          var r = t("inherits"),
            o = t("./lib/ajax-based"),
            i = t("./receiver/xhr"),
            s = t("./sender/xhr-cors"),
            a = t("./sender/xhr-local");

          function l(t) {
            if (!a.enabled && !s.enabled)
              throw new Error("Transport created when disabled");
            o.call(this, t, "/xhr", i, s);
          }

          r(l, o),
            (l.enabled = function (t) {
              return (
                !t.nullOrigin && (!(!a.enabled || !t.sameOrigin) || s.enabled)
              );
            }),
            (l.transportName = "xhr-polling"),
            (l.roundTrips = 2),
            (e.exports = l);
        },
        {
          "./lib/ajax-based": 24,
          "./receiver/xhr": 32,
          "./sender/xhr-cors": 35,
          "./sender/xhr-local": 37,
          inherits: 54,
        },
      ],
      42: [
        function (l, c, t) {
          (function (t) {
            "use strict";
            var e = l("inherits"),
              n = l("./lib/ajax-based"),
              r = l("./receiver/xhr"),
              o = l("./sender/xhr-cors"),
              i = l("./sender/xhr-local"),
              s = l("../utils/browser");

            function a(t) {
              if (!i.enabled && !o.enabled)
                throw new Error("Transport created when disabled");
              n.call(this, t, "/xhr_streaming", r, o);
            }

            e(a, n),
              (a.enabled = function (t) {
                return !t.nullOrigin && !s.isOpera() && o.enabled;
              }),
              (a.transportName = "xhr-streaming"),
              (a.roundTrips = 2),
              (a.needBody = !!t.document),
              (c.exports = a);
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        {
          "../utils/browser": 44,
          "./lib/ajax-based": 24,
          "./receiver/xhr": 32,
          "./sender/xhr-cors": 35,
          "./sender/xhr-local": 37,
          inherits: 54,
        },
      ],
      43: [
        function (t, e, n) {
          (function (n) {
            "use strict";
            n.crypto && n.crypto.getRandomValues
              ? (e.exports.randomBytes = function (t) {
                  var e = new Uint8Array(t);
                  return n.crypto.getRandomValues(e), e;
                })
              : (e.exports.randomBytes = function (t) {
                  for (var e = new Array(t), n = 0; n < t; n++)
                    e[n] = Math.floor(256 * Math.random());
                  return e;
                });
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        {},
      ],
      44: [
        function (t, e, n) {
          (function (t) {
            "use strict";
            e.exports = {
              isOpera: function () {
                return t.navigator && /opera/i.test(t.navigator.userAgent);
              },
              isKonqueror: function () {
                return t.navigator && /konqueror/i.test(t.navigator.userAgent);
              },
              hasDomain: function () {
                if (!t.document) return !0;
                try {
                  return !!t.document.domain;
                } catch (t) {
                  return !1;
                }
              },
            };
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        {},
      ],
      45: [
        function (t, e, n) {
          "use strict";
          var r,
            o = t("json3"),
            i =
              /[\x00-\x1f\ud800-\udfff\ufffe\uffff\u0300-\u0333\u033d-\u0346\u034a-\u034c\u0350-\u0352\u0357-\u0358\u035c-\u0362\u0374\u037e\u0387\u0591-\u05af\u05c4\u0610-\u0617\u0653-\u0654\u0657-\u065b\u065d-\u065e\u06df-\u06e2\u06eb-\u06ec\u0730\u0732-\u0733\u0735-\u0736\u073a\u073d\u073f-\u0741\u0743\u0745\u0747\u07eb-\u07f1\u0951\u0958-\u095f\u09dc-\u09dd\u09df\u0a33\u0a36\u0a59-\u0a5b\u0a5e\u0b5c-\u0b5d\u0e38-\u0e39\u0f43\u0f4d\u0f52\u0f57\u0f5c\u0f69\u0f72-\u0f76\u0f78\u0f80-\u0f83\u0f93\u0f9d\u0fa2\u0fa7\u0fac\u0fb9\u1939-\u193a\u1a17\u1b6b\u1cda-\u1cdb\u1dc0-\u1dcf\u1dfc\u1dfe\u1f71\u1f73\u1f75\u1f77\u1f79\u1f7b\u1f7d\u1fbb\u1fbe\u1fc9\u1fcb\u1fd3\u1fdb\u1fe3\u1feb\u1fee-\u1fef\u1ff9\u1ffb\u1ffd\u2000-\u2001\u20d0-\u20d1\u20d4-\u20d7\u20e7-\u20e9\u2126\u212a-\u212b\u2329-\u232a\u2adc\u302b-\u302c\uaab2-\uaab3\uf900-\ufa0d\ufa10\ufa12\ufa15-\ufa1e\ufa20\ufa22\ufa25-\ufa26\ufa2a-\ufa2d\ufa30-\ufa6d\ufa70-\ufad9\ufb1d\ufb1f\ufb2a-\ufb36\ufb38-\ufb3c\ufb3e\ufb40-\ufb41\ufb43-\ufb44\ufb46-\ufb4e\ufff0-\uffff]/g;
          e.exports = {
            quote: function (t) {
              var e = o.stringify(t);
              return (
                (i.lastIndex = 0),
                i.test(e)
                  ? ((r =
                      r ||
                      (function (t) {
                        var e,
                          n = {},
                          r = [];
                        for (e = 0; e < 65536; e++)
                          r.push(String.fromCharCode(e));
                        return (
                          (t.lastIndex = 0),
                          r.join("").replace(t, function (t) {
                            return (
                              (n[t] =
                                "\\u" +
                                ("0000" + t.charCodeAt(0).toString(16)).slice(
                                  -4,
                                )),
                              ""
                            );
                          }),
                          (t.lastIndex = 0),
                          n
                        );
                      })(i)),
                    e.replace(i, function (t) {
                      return r[t];
                    }))
                  : e
              );
            },
          };
        },
        { json3: 55 },
      ],
      46: [
        function (t, e, n) {
          (function (n) {
            "use strict";
            var r = t("./random"),
              o = {},
              i = !1,
              s = n.chrome && n.chrome.app && n.chrome.app.runtime;
            e.exports = {
              attachEvent: function (t, e) {
                void 0 !== n.addEventListener
                  ? n.addEventListener(t, e, !1)
                  : n.document &&
                    n.attachEvent &&
                    (n.document.attachEvent("on" + t, e),
                    n.attachEvent("on" + t, e));
              },
              detachEvent: function (t, e) {
                void 0 !== n.addEventListener
                  ? n.removeEventListener(t, e, !1)
                  : n.document &&
                    n.detachEvent &&
                    (n.document.detachEvent("on" + t, e),
                    n.detachEvent("on" + t, e));
              },
              unloadAdd: function (t) {
                if (s) return null;
                var e = r.string(8);
                return (
                  (o[e] = t), i && setTimeout(this.triggerUnloadCallbacks, 0), e
                );
              },
              unloadDel: function (t) {
                t in o && delete o[t];
              },
              triggerUnloadCallbacks: function () {
                for (var t in o) o[t](), delete o[t];
              },
            };
            s ||
              e.exports.attachEvent("unload", function () {
                i || ((i = !0), e.exports.triggerUnloadCallbacks());
              });
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        { "./random": 50 },
      ],
      47: [
        function (e, p, t) {
          (function (f) {
            "use strict";
            var h = e("./event"),
              n = e("json3"),
              t = e("./browser"),
              d = function () {};
            (p.exports = {
              WPrefix: "_jp",
              currentWindowId: null,
              polluteGlobalNamespace: function () {
                p.exports.WPrefix in f || (f[p.exports.WPrefix] = {});
              },
              postMessage: function (t, e) {
                f.parent !== f
                  ? f.parent.postMessage(
                      n.stringify({
                        windowId: p.exports.currentWindowId,
                        type: t,
                        data: e || "",
                      }),
                      "*",
                    )
                  : d("Cannot postMessage, no parent window.", t, e);
              },
              createIframe: function (t, e) {
                function n() {
                  d("unattach"), clearTimeout(i);
                  try {
                    a.onload = null;
                  } catch (t) {}
                  a.onerror = null;
                }

                function r() {
                  d("cleanup"),
                    a &&
                      (n(),
                      setTimeout(function () {
                        a && a.parentNode.removeChild(a), (a = null);
                      }, 0),
                      h.unloadDel(s));
                }

                function o(t) {
                  d("onerror", t), a && (r(), e(t));
                }

                var i,
                  s,
                  a = f.document.createElement("iframe");
                return (
                  (a.src = t),
                  (a.style.display = "none"),
                  (a.style.position = "absolute"),
                  (a.onerror = function () {
                    o("onerror");
                  }),
                  (a.onload = function () {
                    d("onload"),
                      clearTimeout(i),
                      (i = setTimeout(function () {
                        o("onload timeout");
                      }, 2e3));
                  }),
                  f.document.body.appendChild(a),
                  (i = setTimeout(function () {
                    o("timeout");
                  }, 15e3)),
                  (s = h.unloadAdd(r)),
                  {
                    post: function (t, e) {
                      d("post", t, e),
                        setTimeout(function () {
                          try {
                            a &&
                              a.contentWindow &&
                              a.contentWindow.postMessage(t, e);
                          } catch (t) {}
                        }, 0);
                    },
                    cleanup: r,
                    loaded: n,
                  }
                );
              },
              createHtmlfile: function (t, e) {
                function n() {
                  clearTimeout(i), (a.onerror = null);
                }

                function r() {
                  c &&
                    (n(),
                    h.unloadDel(s),
                    a.parentNode.removeChild(a),
                    (a = c = null),
                    CollectGarbage());
                }

                function o(t) {
                  d("onerror", t), c && (r(), e(t));
                }

                var i,
                  s,
                  a,
                  l = ["Active"].concat("Object").join("X"),
                  c = new f[l]("htmlfile");
                c.open(),
                  c.write(
                    '<html><script>document.domain="' +
                      f.document.domain +
                      '";</script></html>',
                  ),
                  c.close(),
                  (c.parentWindow[p.exports.WPrefix] = f[p.exports.WPrefix]);
                var u = c.createElement("div");
                return (
                  c.body.appendChild(u),
                  (a = c.createElement("iframe")),
                  u.appendChild(a),
                  (a.src = t),
                  (a.onerror = function () {
                    o("onerror");
                  }),
                  (i = setTimeout(function () {
                    o("timeout");
                  }, 15e3)),
                  (s = h.unloadAdd(r)),
                  {
                    post: function (t, e) {
                      try {
                        setTimeout(function () {
                          a &&
                            a.contentWindow &&
                            a.contentWindow.postMessage(t, e);
                        }, 0);
                      } catch (t) {}
                    },
                    cleanup: r,
                    loaded: n,
                  }
                );
              },
            }),
              (p.exports.iframeEnabled = !1),
              f.document &&
                (p.exports.iframeEnabled =
                  ("function" == typeof f.postMessage ||
                    "object" == typeof f.postMessage) &&
                  !t.isKonqueror());
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        { "./browser": 44, "./event": 46, debug: void 0, json3: 55 },
      ],
      48: [
        function (t, e, n) {
          (function (n) {
            "use strict";
            var r = {};
            ["log", "debug", "warn"].forEach(function (t) {
              var e;
              try {
                e = n.console && n.console[t] && n.console[t].apply;
              } catch (t) {}
              r[t] = e
                ? function () {
                    return n.console[t].apply(n.console, arguments);
                  }
                : "log" === t
                ? function () {}
                : r.log;
            }),
              (e.exports = r);
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        {},
      ],
      49: [
        function (t, e, n) {
          "use strict";
          e.exports = {
            isObject: function (t) {
              var e = typeof t;
              return "function" == e || ("object" == e && !!t);
            },
            extend: function (t) {
              if (!this.isObject(t)) return t;
              for (var e, n, r = 1, o = arguments.length; r < o; r++)
                for (n in (e = arguments[r]))
                  Object.prototype.hasOwnProperty.call(e, n) && (t[n] = e[n]);
              return t;
            },
          };
        },
        {},
      ],
      50: [
        function (t, e, n) {
          "use strict";
          var i = t("crypto"),
            s = "abcdefghijklmnopqrstuvwxyz012345";
          e.exports = {
            string: function (t) {
              for (
                var e = s.length, n = i.randomBytes(t), r = [], o = 0;
                o < t;
                o++
              )
                r.push(s.substr(n[o] % e, 1));
              return r.join("");
            },
            number: function (t) {
              return Math.floor(Math.random() * t);
            },
            numberString: function (t) {
              var e = ("" + (t - 1)).length;
              return (new Array(e + 1).join("0") + this.number(t)).slice(-e);
            },
          };
        },
        { crypto: 43 },
      ],
      51: [
        function (t, e, n) {
          "use strict";
          var o = function () {};
          e.exports = function (t) {
            return {
              filterToEnabled: function (e, n) {
                var r = { main: [], facade: [] };
                return (
                  e ? "string" == typeof e && (e = [e]) : (e = []),
                  t.forEach(function (t) {
                    t &&
                      ("websocket" !== t.transportName || !1 !== n.websocket
                        ? e.length && -1 === e.indexOf(t.transportName)
                          ? o("not in whitelist", t.transportName)
                          : t.enabled(n)
                          ? (o("enabled", t.transportName),
                            r.main.push(t),
                            t.facadeTransport &&
                              r.facade.push(t.facadeTransport))
                          : o("disabled", t.transportName)
                        : o("disabled from server", "websocket"));
                  }),
                  r
                );
              },
            };
          };
        },
        { debug: void 0 },
      ],
      52: [
        function (t, e, n) {
          "use strict";
          var r = t("url-parse"),
            o = function () {};
          e.exports = {
            getOrigin: function (t) {
              if (!t) return null;
              var e = new r(t);
              if ("file:" === e.protocol) return null;
              var n = e.port;
              return (
                (n = n || ("https:" === e.protocol ? "443" : "80")),
                e.protocol + "//" + e.hostname + ":" + n
              );
            },
            isOriginEqual: function (t, e) {
              var n = this.getOrigin(t) === this.getOrigin(e);
              return o("same", t, e, n), n;
            },
            isSchemeEqual: function (t, e) {
              return t.split(":")[0] === e.split(":")[0];
            },
            addPath: function (t, e) {
              var n = t.split("?");
              return n[0] + e + (n[1] ? "?" + n[1] : "");
            },
            addQuery: function (t, e) {
              return t + (-1 === t.indexOf("?") ? "?" + e : "&" + e);
            },
          };
        },
        { debug: void 0, "url-parse": 58 },
      ],
      53: [
        function (t, e, n) {
          e.exports = "1.4.0";
        },
        {},
      ],
      54: [
        function (t, e, n) {
          "function" == typeof Object.create
            ? (e.exports = function (t, e) {
                (t.super_ = e),
                  (t.prototype = Object.create(e.prototype, {
                    constructor: {
                      value: t,
                      enumerable: !1,
                      writable: !0,
                      configurable: !0,
                    },
                  }));
              })
            : (e.exports = function (t, e) {
                t.super_ = e;

                function n() {}

                (n.prototype = e.prototype),
                  (t.prototype = new n()),
                  (t.prototype.constructor = t);
              });
        },
        {},
      ],
      55: [
        function (t, a, l) {
          (function (s) {
            (function () {
              var q = { function: !0, object: !0 },
                t = q[typeof l] && l && !l.nodeType && l,
                D = (q[typeof window] && window) || this,
                e =
                  t &&
                  q[typeof a] &&
                  a &&
                  !a.nodeType &&
                  "object" == typeof s &&
                  s;

              function W(t, l) {
                (t = t || D.Object()), (l = l || D.Object());
                var c = t.Number || D.Number,
                  u = t.String || D.String,
                  e = t.Object || D.Object,
                  f = t.Date || D.Date,
                  n = t.SyntaxError || D.SyntaxError,
                  E = t.TypeError || D.TypeError,
                  r = t.Math || D.Math,
                  o = t.JSON || D.JSON;
                "object" == typeof o &&
                  o &&
                  ((l.stringify = o.stringify), (l.parse = o.parse));
                var j,
                  S,
                  T,
                  i = e.prototype,
                  O = i.toString,
                  h = new f(-0xc782b5b800cec);
                try {
                  h =
                    -109252 == h.getUTCFullYear() &&
                    0 === h.getUTCMonth() &&
                    1 === h.getUTCDate() &&
                    10 == h.getUTCHours() &&
                    37 == h.getUTCMinutes() &&
                    6 == h.getUTCSeconds() &&
                    708 == h.getUTCMilliseconds();
                } catch (t) {}

                function d(t) {
                  if (d[t] !== T) return d[t];
                  var e;
                  if ("bug-string-char-index" == t) e = "a" != "a"[0];
                  else if ("json" == t)
                    e = d("json-stringify") && d("json-parse");
                  else {
                    var n,
                      r = '{"a":[1,true,false,null,"\\u0000\\b\\n\\f\\r\\t"]}';
                    if ("json-stringify" == t) {
                      var o = l.stringify,
                        i = "function" == typeof o && h;
                      if (i) {
                        (n = function () {
                          return 1;
                        }).toJSON = n;
                        try {
                          i =
                            "0" === o(0) &&
                            "0" === o(new c()) &&
                            '""' == o(new u()) &&
                            o(O) === T &&
                            o(T) === T &&
                            o() === T &&
                            "1" === o(n) &&
                            "[1]" == o([n]) &&
                            "[null]" == o([T]) &&
                            "null" == o(null) &&
                            "[null,null,null]" == o([T, O, null]) &&
                            o({ a: [n, !0, !1, null, "\0\b\n\f\r\t"] }) == r &&
                            "1" === o(null, n) &&
                            "[\n 1,\n 2\n]" == o([1, 2], null, 1) &&
                            '"-271821-04-20T00:00:00.000Z"' ==
                              o(new f(-864e13)) &&
                            '"+275760-09-13T00:00:00.000Z"' ==
                              o(new f(864e13)) &&
                            '"-000001-01-01T00:00:00.000Z"' ==
                              o(new f(-621987552e5)) &&
                            '"1969-12-31T23:59:59.999Z"' == o(new f(-1));
                        } catch (t) {
                          i = !1;
                        }
                      }
                      e = i;
                    }
                    if ("json-parse" == t) {
                      var s = l.parse;
                      if ("function" == typeof s)
                        try {
                          if (0 === s("0") && !s(!1)) {
                            var a = 5 == (n = s(r)).a.length && 1 === n.a[0];
                            if (a) {
                              try {
                                a = !s('"\t"');
                              } catch (t) {}
                              if (a)
                                try {
                                  a = 1 !== s("01");
                                } catch (t) {}
                              if (a)
                                try {
                                  a = 1 !== s("1.");
                                } catch (t) {}
                            }
                          }
                        } catch (t) {
                          a = !1;
                        }
                      e = a;
                    }
                  }
                  return (d[t] = !!e);
                }

                if (!d("json")) {
                  var p = "[object Function]",
                    C = "[object Number]",
                    N = "[object String]",
                    A = "[object Array]",
                    a = d("bug-string-char-index");
                  if (!h)
                    var k = r.floor,
                      s = [
                        0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334,
                      ],
                      I = function (t, e) {
                        return (
                          s[e] +
                          365 * (t - 1970) +
                          k((t - 1969 + (e = +(1 < e))) / 4) -
                          k((t - 1901 + e) / 100) +
                          k((t - 1601 + e) / 400)
                        );
                      };
                  if (
                    ((j = i.hasOwnProperty) ||
                      (j = function (t) {
                        var n,
                          e = {};
                        return (
                          (j =
                            ((e.__proto__ = null),
                            (e.__proto__ = { toString: 1 }),
                            e).toString != O
                              ? function (t) {
                                  var e = this.__proto__,
                                    n = t in ((this.__proto__ = null), this);
                                  return (this.__proto__ = e), n;
                                }
                              : ((n = e.constructor),
                                function (t) {
                                  var e = (this.constructor || n).prototype;
                                  return (
                                    t in this && !(t in e && this[t] === e[t])
                                  );
                                })),
                          (e = null),
                          j.call(this, t)
                        );
                      }),
                    (S = function (t, e) {
                      var n,
                        s,
                        r,
                        o = 0;
                      for (r in (((n = function () {
                        this.valueOf = 0;
                      }).prototype.valueOf = 0),
                      (s = new n())))
                        j.call(s, r) && o++;
                      return (
                        (n = s = null),
                        (S = o
                          ? 2 == o
                            ? function (t, e) {
                                var n,
                                  r = {},
                                  o = O.call(t) == p;
                                for (n in t)
                                  (o && "prototype" == n) ||
                                    j.call(r, n) ||
                                    !(r[n] = 1) ||
                                    !j.call(t, n) ||
                                    e(n);
                              }
                            : function (t, e) {
                                var n,
                                  r,
                                  o = O.call(t) == p;
                                for (n in t)
                                  (o && "prototype" == n) ||
                                    !j.call(t, n) ||
                                    (r = "constructor" === n) ||
                                    e(n);
                                (r || j.call(t, (n = "constructor"))) && e(n);
                              }
                          : ((s = [
                              "valueOf",
                              "toString",
                              "toLocaleString",
                              "propertyIsEnumerable",
                              "isPrototypeOf",
                              "hasOwnProperty",
                              "constructor",
                            ]),
                            function (t, e) {
                              var n,
                                r,
                                o = O.call(t) == p,
                                i =
                                  (!o &&
                                    "function" != typeof t.constructor &&
                                    q[typeof t.hasOwnProperty] &&
                                    t.hasOwnProperty) ||
                                  j;
                              for (n in t)
                                (o && "prototype" == n) ||
                                  !i.call(t, n) ||
                                  e(n);
                              for (
                                r = s.length;
                                (n = s[--r]);
                                i.call(t, n) && e(n)
                              );
                            }))(t, e)
                      );
                    }),
                    !d("json-stringify"))
                  ) {
                    function P(t, e) {
                      return ("000000" + (e || 0)).slice(-t);
                    }

                    function L(t) {
                      for (
                        var e = '"',
                          n = 0,
                          r = t.length,
                          o = !a || 10 < r,
                          i = o && (a ? t.split("") : t);
                        n < r;
                        n++
                      ) {
                        var s = t.charCodeAt(n);
                        switch (s) {
                          case 8:
                          case 9:
                          case 10:
                          case 12:
                          case 13:
                          case 34:
                          case 92:
                            e += m[s];
                            break;
                          default:
                            if (s < 32) {
                              e += "\\u00" + P(2, s.toString(16));
                              break;
                            }
                            e += o ? i[n] : t.charAt(n);
                        }
                      }
                      return e + '"';
                    }

                    var m = {
                        92: "\\\\",
                        34: '\\"',
                        8: "\\b",
                        12: "\\f",
                        10: "\\n",
                        13: "\\r",
                        9: "\\t",
                      },
                      R = function (t, e, n, r, o, i, s) {
                        var a, l, c, u, f, h, d, p, m, v, b, y, g, w, x, _;
                        try {
                          a = e[t];
                        } catch (t) {}
                        if ("object" == typeof a && a)
                          if (
                            "[object Date]" != (l = O.call(a)) ||
                            j.call(a, "toJSON")
                          )
                            "function" == typeof a.toJSON &&
                              ((l != C && l != N && l != A) ||
                                j.call(a, "toJSON")) &&
                              (a = a.toJSON(t));
                          else if (-1 / 0 < a && a < 1 / 0) {
                            if (I) {
                              for (
                                f = k(a / 864e5),
                                  c = k(f / 365.2425) + 1970 - 1;
                                I(c + 1, 0) <= f;
                                c++
                              );
                              for (
                                u = k((f - I(c, 0)) / 30.42);
                                I(c, u + 1) <= f;
                                u++
                              );
                              (f = 1 + f - I(c, u)),
                                (d =
                                  k(
                                    (h = ((a % 864e5) + 864e5) % 864e5) / 36e5,
                                  ) % 24),
                                (p = k(h / 6e4) % 60),
                                (m = k(h / 1e3) % 60),
                                (v = h % 1e3);
                            } else
                              (c = a.getUTCFullYear()),
                                (u = a.getUTCMonth()),
                                (f = a.getUTCDate()),
                                (d = a.getUTCHours()),
                                (p = a.getUTCMinutes()),
                                (m = a.getUTCSeconds()),
                                (v = a.getUTCMilliseconds());
                            a =
                              (c <= 0 || 1e4 <= c
                                ? (c < 0 ? "-" : "+") + P(6, c < 0 ? -c : c)
                                : P(4, c)) +
                              "-" +
                              P(2, u + 1) +
                              "-" +
                              P(2, f) +
                              "T" +
                              P(2, d) +
                              ":" +
                              P(2, p) +
                              ":" +
                              P(2, m) +
                              "." +
                              P(3, v) +
                              "Z";
                          } else a = null;
                        if ((n && (a = n.call(e, t, a)), null === a))
                          return "null";
                        if ("[object Boolean]" == (l = O.call(a)))
                          return "" + a;
                        if (l == C)
                          return -1 / 0 < a && a < 1 / 0 ? "" + a : "null";
                        if (l == N) return L("" + a);
                        if ("object" == typeof a) {
                          for (w = s.length; w--; ) if (s[w] === a) throw E();
                          if (
                            (s.push(a), (b = []), (x = i), (i += o), l == A)
                          ) {
                            for (g = 0, w = a.length; g < w; g++)
                              (y = R(g, a, n, r, o, i, s)),
                                b.push(y === T ? "null" : y);
                            _ = b.length
                              ? o
                                ? "[\n" + i + b.join(",\n" + i) + "\n" + x + "]"
                                : "[" + b.join(",") + "]"
                              : "[]";
                          } else
                            S(r || a, function (t) {
                              var e = R(t, a, n, r, o, i, s);
                              e !== T &&
                                b.push(L(t) + ":" + (o ? " " : "") + e);
                            }),
                              (_ = b.length
                                ? o
                                  ? "{\n" +
                                    i +
                                    b.join(",\n" + i) +
                                    "\n" +
                                    x +
                                    "}"
                                  : "{" + b.join(",") + "}"
                                : "{}");
                          return s.pop(), _;
                        }
                      };
                    l.stringify = function (t, e, n) {
                      var r, o, i, s;
                      if (q[typeof e] && e)
                        if ((s = O.call(e)) == p) o = e;
                        else if (s == A) {
                          i = {};
                          for (
                            var a, l = 0, c = e.length;
                            l < c;
                            a = e[l++],
                              ((s = O.call(a)) != N && s != C) || (i[a] = 1)
                          );
                        }
                      if (n)
                        if ((s = O.call(n)) == C) {
                          if (0 < (n -= n % 1))
                            for (
                              r = "", 10 < n && (n = 10);
                              r.length < n;
                              r += " "
                            );
                        } else
                          s == N && (r = n.length <= 10 ? n : n.slice(0, 10));
                      return R("", (((a = {})[""] = t), a), o, i, r, "", []);
                    };
                  }
                  if (!d("json-parse")) {
                    function v() {
                      throw ((g = w = null), n());
                    }

                    function b() {
                      for (var t, e, n, r, o, i = w, s = i.length; g < s; )
                        switch ((o = i.charCodeAt(g))) {
                          case 9:
                          case 10:
                          case 13:
                          case 32:
                            g++;
                            break;
                          case 123:
                          case 125:
                          case 91:
                          case 93:
                          case 58:
                          case 44:
                            return (t = a ? i.charAt(g) : i[g]), g++, t;
                          case 34:
                            for (t = "@", g++; g < s; )
                              if ((o = i.charCodeAt(g)) < 32) v();
                              else if (92 == o)
                                switch ((o = i.charCodeAt(++g))) {
                                  case 92:
                                  case 34:
                                  case 47:
                                  case 98:
                                  case 116:
                                  case 110:
                                  case 102:
                                  case 114:
                                    (t += _[o]), g++;
                                    break;
                                  case 117:
                                    for (e = ++g, n = g + 4; g < n; g++)
                                      (48 <= (o = i.charCodeAt(g)) &&
                                        o <= 57) ||
                                        (97 <= o && o <= 102) ||
                                        (65 <= o && o <= 70) ||
                                        v();
                                    t += x("0x" + i.slice(e, g));
                                    break;
                                  default:
                                    v();
                                }
                              else {
                                if (34 == o) break;
                                for (
                                  o = i.charCodeAt(g), e = g;
                                  32 <= o && 92 != o && 34 != o;

                                )
                                  o = i.charCodeAt(++g);
                                t += i.slice(e, g);
                              }
                            if (34 == i.charCodeAt(g)) return g++, t;
                            v();
                          default:
                            if (
                              ((e = g),
                              45 == o && ((r = !0), (o = i.charCodeAt(++g))),
                              48 <= o && o <= 57)
                            ) {
                              for (
                                48 == o &&
                                  48 <= (o = i.charCodeAt(g + 1)) &&
                                  o <= 57 &&
                                  v(),
                                  r = !1;
                                g < s && 48 <= (o = i.charCodeAt(g)) && o <= 57;
                                g++
                              );
                              if (46 == i.charCodeAt(g)) {
                                for (
                                  n = ++g;
                                  n < s &&
                                  48 <= (o = i.charCodeAt(n)) &&
                                  o <= 57;
                                  n++
                                );
                                n == g && v(), (g = n);
                              }
                              if (101 == (o = i.charCodeAt(g)) || 69 == o) {
                                for (
                                  (43 != (o = i.charCodeAt(++g)) && 45 != o) ||
                                    g++,
                                    n = g;
                                  n < s &&
                                  48 <= (o = i.charCodeAt(n)) &&
                                  o <= 57;
                                  n++
                                );
                                n == g && v(), (g = n);
                              }
                              return +i.slice(e, g);
                            }
                            if ((r && v(), "true" == i.slice(g, g + 4)))
                              return (g += 4), !0;
                            if ("false" == i.slice(g, g + 5))
                              return (g += 5), !1;
                            if ("null" == i.slice(g, g + 4))
                              return (g += 4), null;
                            v();
                        }
                      return "$";
                    }

                    function y(t, e, n) {
                      var r = M(t, e, n);
                      r === T ? delete t[e] : (t[e] = r);
                    }

                    var g,
                      w,
                      x = u.fromCharCode,
                      _ = {
                        92: "\\",
                        34: '"',
                        47: "/",
                        98: "\b",
                        116: "\t",
                        110: "\n",
                        102: "\f",
                        114: "\r",
                      },
                      U = function (t) {
                        var e, n;
                        if (("$" == t && v(), "string" == typeof t)) {
                          if ("@" == (a ? t.charAt(0) : t[0]))
                            return t.slice(1);
                          if ("[" == t) {
                            for (e = []; "]" != (t = b()); n = n || !0)
                              n && ("," == t ? "]" == (t = b()) && v() : v()),
                                "," == t && v(),
                                e.push(U(t));
                            return e;
                          }
                          if ("{" == t) {
                            for (e = {}; "}" != (t = b()); n = n || !0)
                              n && ("," == t ? "}" == (t = b()) && v() : v()),
                                ("," != t &&
                                  "string" == typeof t &&
                                  "@" == (a ? t.charAt(0) : t[0]) &&
                                  ":" == b()) ||
                                  v(),
                                (e[t.slice(1)] = U(b()));
                            return e;
                          }
                          v();
                        }
                        return t;
                      },
                      M = function (t, e, n) {
                        var r,
                          o = t[e];
                        if ("object" == typeof o && o)
                          if (O.call(o) == A)
                            for (r = o.length; r--; ) y(o, r, n);
                          else
                            S(o, function (t) {
                              y(o, t, n);
                            });
                        return n.call(t, e, o);
                      };
                    l.parse = function (t, e) {
                      var n, r;
                      return (
                        (g = 0),
                        (w = "" + t),
                        (n = U(b())),
                        "$" != b() && v(),
                        (g = w = null),
                        e && O.call(e) == p
                          ? M((((r = {})[""] = n), r), "", e)
                          : n
                      );
                    };
                  }
                }
                return (l.runInContext = W), l;
              }

              if (
                (!e ||
                  (e.global !== e && e.window !== e && e.self !== e) ||
                  (D = e),
                t)
              )
                W(D, t);
              else {
                var n = D.JSON,
                  r = D.JSON3,
                  o = !1,
                  i = W(
                    D,
                    (D.JSON3 = {
                      noConflict: function () {
                        return (
                          o ||
                            ((o = !0),
                            (D.JSON = n),
                            (D.JSON3 = r),
                            (n = r = null)),
                          i
                        );
                      },
                    }),
                  );
                D.JSON = { parse: i.parse, stringify: i.stringify };
              }
            }).call(this);
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        {},
      ],
      56: [
        function (t, e, n) {
          "use strict";
          var o = Object.prototype.hasOwnProperty;

          function s(t) {
            return decodeURIComponent(t.replace(/\+/g, " "));
          }

          (n.stringify = function (t, e) {
            e = e || "";
            var n = [];
            for (var r in ("string" != typeof e && (e = "?"), t))
              o.call(t, r) &&
                n.push(encodeURIComponent(r) + "=" + encodeURIComponent(t[r]));
            return n.length ? e + n.join("&") : "";
          }),
            (n.parse = function (t) {
              for (
                var e, n = /([^=?&]+)=?([^&]*)/g, r = {};
                (e = n.exec(t));

              ) {
                var o = s(e[1]),
                  i = s(e[2]);
                o in r || (r[o] = i);
              }
              return r;
            });
        },
        {},
      ],
      57: [
        function (t, e, n) {
          "use strict";
          e.exports = function (t, e) {
            if (((e = e.split(":")[0]), !(t = +t))) return !1;
            switch (e) {
              case "http":
              case "ws":
                return 80 !== t;
              case "https":
              case "wss":
                return 443 !== t;
              case "ftp":
                return 21 !== t;
              case "gopher":
                return 70 !== t;
              case "file":
                return !1;
            }
            return 0 !== t;
          };
        },
        {},
      ],
      58: [
        function (t, e, n) {
          (function (i) {
            "use strict";
            var d = t("requires-port"),
              p = t("querystringify"),
              n = /^([a-z][a-z0-9.+-]*:)?(\/\/)?([\S\s]*)/i,
              s = /^[A-Za-z][A-Za-z0-9+-.]*:\/\//,
              m = [
                ["#", "hash"],
                ["?", "query"],
                function (t) {
                  return t.replace("\\", "/");
                },
                ["/", "pathname"],
                ["@", "auth", 1],
                [NaN, "host", void 0, 1, 1],
                [/:(\d+)$/, "port", void 0, 1],
                [NaN, "hostname", void 0, 1, 1],
              ],
              a = { hash: 1, query: 1 };

            function v(t) {
              var e,
                n = (i && i.location) || {},
                r = {},
                o = typeof (t = t || n);
              if ("blob:" === t.protocol) r = new y(unescape(t.pathname), {});
              else if ("string" == o)
                for (e in ((r = new y(t, {})), a)) delete r[e];
              else if ("object" == o) {
                for (e in t) e in a || (r[e] = t[e]);
                void 0 === r.slashes && (r.slashes = s.test(t.href));
              }
              return r;
            }

            function b(t) {
              var e = n.exec(t);
              return {
                protocol: e[1] ? e[1].toLowerCase() : "",
                slashes: !!e[2],
                rest: e[3],
              };
            }

            function y(t, e, n) {
              if (!(this instanceof y)) return new y(t, e, n);
              var r,
                o,
                i,
                s,
                a,
                l,
                c = m.slice(),
                u = typeof e,
                f = this,
                h = 0;
              for (
                "object" != u && "string" != u && ((n = e), (e = null)),
                  n && "function" != typeof n && (n = p.parse),
                  e = v(e),
                  r = !(o = b(t || "")).protocol && !o.slashes,
                  f.slashes = o.slashes || (r && e.slashes),
                  f.protocol = o.protocol || e.protocol || "",
                  t = o.rest,
                  o.slashes || (c[3] = [/(.*)/, "pathname"]);
                h < c.length;
                h++
              )
                "function" != typeof (s = c[h])
                  ? ((i = s[0]),
                    (l = s[1]),
                    i != i
                      ? (f[l] = t)
                      : "string" == typeof i
                      ? ~(a = t.indexOf(i)) &&
                        (t =
                          "number" == typeof s[2]
                            ? ((f[l] = t.slice(0, a)), t.slice(a + s[2]))
                            : ((f[l] = t.slice(a)), t.slice(0, a)))
                      : (a = i.exec(t)) &&
                        ((f[l] = a[1]), (t = t.slice(0, a.index))),
                    (f[l] = f[l] || (r && s[3] && e[l]) || ""),
                    s[4] && (f[l] = f[l].toLowerCase()))
                  : (t = s(t));
              n && (f.query = n(f.query)),
                r &&
                  e.slashes &&
                  "/" !== f.pathname.charAt(0) &&
                  ("" !== f.pathname || "" !== e.pathname) &&
                  (f.pathname = (function (t, e) {
                    for (
                      var n = (e || "/")
                          .split("/")
                          .slice(0, -1)
                          .concat(t.split("/")),
                        r = n.length,
                        o = n[r - 1],
                        i = !1,
                        s = 0;
                      r--;

                    )
                      "." === n[r]
                        ? n.splice(r, 1)
                        : ".." === n[r]
                        ? (n.splice(r, 1), s++)
                        : s && (0 === r && (i = !0), n.splice(r, 1), s--);
                    return (
                      i && n.unshift(""),
                      ("." !== o && ".." !== o) || n.push(""),
                      n.join("/")
                    );
                  })(f.pathname, e.pathname)),
                d(f.port, f.protocol) || ((f.host = f.hostname), (f.port = "")),
                (f.username = f.password = ""),
                f.auth &&
                  ((s = f.auth.split(":")),
                  (f.username = s[0] || ""),
                  (f.password = s[1] || "")),
                (f.origin =
                  f.protocol && f.host && "file:" !== f.protocol
                    ? f.protocol + "//" + f.host
                    : "null"),
                (f.href = f.toString());
            }

            (y.prototype = {
              set: function (t, e, n) {
                var r = this;
                switch (t) {
                  case "query":
                    "string" == typeof e && e.length && (e = (n || p.parse)(e)),
                      (r[t] = e);
                    break;
                  case "port":
                    (r[t] = e),
                      d(e, r.protocol)
                        ? e && (r.host = r.hostname + ":" + e)
                        : ((r.host = r.hostname), (r[t] = ""));
                    break;
                  case "hostname":
                    (r[t] = e), r.port && (e += ":" + r.port), (r.host = e);
                    break;
                  case "host":
                    (r[t] = e),
                      /:\d+$/.test(e)
                        ? ((e = e.split(":")),
                          (r.port = e.pop()),
                          (r.hostname = e.join(":")))
                        : ((r.hostname = e), (r.port = ""));
                    break;
                  case "protocol":
                    (r.protocol = e.toLowerCase()), (r.slashes = !n);
                    break;
                  case "pathname":
                  case "hash":
                    if (e) {
                      var o = "pathname" === t ? "/" : "#";
                      r[t] = e.charAt(0) !== o ? o + e : e;
                    } else r[t] = e;
                    break;
                  default:
                    r[t] = e;
                }
                for (var i = 0; i < m.length; i++) {
                  var s = m[i];
                  s[4] && (r[s[1]] = r[s[1]].toLowerCase());
                }
                return (
                  (r.origin =
                    r.protocol && r.host && "file:" !== r.protocol
                      ? r.protocol + "//" + r.host
                      : "null"),
                  (r.href = r.toString()),
                  r
                );
              },
              toString: function (t) {
                (t && "function" == typeof t) || (t = p.stringify);
                var e,
                  n = this,
                  r = n.protocol;
                r && ":" !== r.charAt(r.length - 1) && (r += ":");
                var o = r + (n.slashes ? "//" : "");
                return (
                  n.username &&
                    ((o += n.username),
                    n.password && (o += ":" + n.password),
                    (o += "@")),
                  (o += n.host + n.pathname),
                  (e = "object" == typeof n.query ? t(n.query) : n.query) &&
                    (o += "?" !== e.charAt(0) ? "?" + e : e),
                  n.hash && (o += n.hash),
                  o
                );
              },
            }),
              (y.extractProtocol = b),
              (y.location = v),
              (y.qs = p),
              (e.exports = y);
          }).call(
            this,
            "undefined" != typeof global
              ? global
              : "undefined" != typeof self
              ? self
              : "undefined" != typeof window
              ? window
              : {},
          );
        },
        { querystringify: 56, "requires-port": 57 },
      ],
    },
    {},
    [1],
  )(1);
});
//# sourceMappingURL=sockjs.min.js.map
