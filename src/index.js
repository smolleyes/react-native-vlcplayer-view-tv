export * from './LitePlayerView';
export { PlayerView, useVideoPlayer } from './Player';
export { VideoView } from './VideoView';

// Export types as JSDoc comments for better IDE support
/**
 * @typedef {Object} VideoSource
 * @property {string} [uri] - The URI of the video
 * @property {number} [time] - The time in milliseconds at which playback should start
 */

/**
 * @typedef {Object} Track
 * @property {string} id - Track identifier
 * @property {string} name - Track name
 */

/**
 * @typedef {Object} VideoInfo
 * @property {Track} track - Current video track
 * @property {Object} videoSize - Video dimensions
 * @property {number} videoSize.width - Video width
 * @property {number} videoSize.height - Video height
 * @property {boolean} seekable - Whether the video is seekable
 * @property {number} duration - Video duration in milliseconds
 * @property {Track[]} audioTracks - Available audio tracks
 * @property {Track[]} textTracks - Available text tracks
 */

/**
 * @typedef {Object} ProgressInfo
 * @property {number} time - Current playback time in milliseconds
 * @property {number} position - Current playback position (0-1)
 */
