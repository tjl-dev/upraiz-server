import { IVoteTarget } from '@/shared/model/vote-target.model';
import { IVotePayout } from '@/shared/model/vote-payout.model';
import { IVoter } from '@/shared/model/voter.model';

export interface IVote {
  id?: number;
  votedTimestamp?: Date;
  verified?: boolean;
  verifiedTime?: Date;
  verifiedBy?: string;
  paid?: boolean;
  voteTarget?: IVoteTarget | null;
  votePayout?: IVotePayout | null;
  voter?: IVoter | null;
}

export class Vote implements IVote {
  constructor(
    public id?: number,
    public votedTimestamp?: Date,
    public verified?: boolean,
    public verifiedTime?: Date,
    public verifiedBy?: string,
    public paid?: boolean,
    public voteTarget?: IVoteTarget | null,
    public votePayout?: IVotePayout | null,
    public voter?: IVoter | null
  ) {
    this.verified = this.verified ?? false;
    this.paid = this.paid ?? false;
  }
}
