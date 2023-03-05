import { IVotePayout } from '@/shared/model/vote-payout.model';
import { IVoteTarget } from '@/shared/model/vote-target.model';
import { IVoter } from '@/shared/model/voter.model';

export interface IVote {
  id?: number;
  votedTimestamp?: Date;
  verified?: boolean | null;
  verifiedTime?: Date | null;
  verifiedBy?: string | null;
  paid?: boolean;
  votePayout?: IVotePayout | null;
  voteTarget?: IVoteTarget | null;
  voter?: IVoter | null;
}

export class Vote implements IVote {
  constructor(
    public id?: number,
    public votedTimestamp?: Date,
    public verified?: boolean | null,
    public verifiedTime?: Date | null,
    public verifiedBy?: string | null,
    public paid?: boolean,
    public votePayout?: IVotePayout | null,
    public voteTarget?: IVoteTarget | null,
    public voter?: IVoter | null
  ) {
    this.verified = this.verified ?? false;
    this.paid = this.paid ?? false;
  }
}
